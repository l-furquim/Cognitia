package org.cognitia.encoder.application.usecases;

import jakarta.annotation.Nullable;
import org.cognitia.encoder.application.gateways.MessagingGateway;
import org.cognitia.encoder.application.gateways.S3Gateway;
import org.cognitia.encoder.application.gateways.VideoGateway;
import org.cognitia.encoder.domain.enums.ProcessStatus;
import org.cognitia.encoder.domain.model.Video;
import org.cognitia.encoder.domain.exceptions.*;
import org.cognitia.encoder.infra.dto.video.*;
import org.cognitia.encoder.utils.VideoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class VideoUseCase {

    private static final Logger log = LoggerFactory.getLogger(VideoUseCase.class);
    private final VideoGateway videoGateway;
    private final S3Gateway s3Gateway;
    private final VideoUtils videoUtils;
    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private final MessagingGateway messagingGateway;


    public VideoUseCase(VideoGateway videoGateway, S3Gateway s3Gateway, VideoUtils videoUtils, MessagingGateway messagingGateway) {
        this.videoGateway = videoGateway;
        this.s3Gateway = s3Gateway;
        this.videoUtils = videoUtils;
        this.messagingGateway = messagingGateway;
    }

    public String uploadVideo(MultipartFile video, UploadVideoInfos infos) {
        if(infos.courseId() == null || infos.courseId() < 0) {
            throw new InvalidVideoUploadException("Course id must be valid.");
        }

        if(
                video.getContentType() == null
        ) {
            throw new InvalidVideoUploadException("The video must be in .mp4 extension and be a valid video.");
        }

        String videoId = UUID.randomUUID().toString();

        Video videoToBeUploaded = new Video(
                videoId,
                video.getOriginalFilename(),
                ProcessStatus.PENDING,
                null,
                video.getContentType(),
                null,
                null,
                null,
                video.getSize(),
                "NONE"
        );
        log.info("AQUI> {}", videoToBeUploaded);
        videoGateway.upload(videoToBeUploaded);

        log.info("Starting async upload to S3 for courseId={}, lessonName={}", infos.courseId(), infos.lessonName());

        CompletableFuture.runAsync(() -> {
            s3Gateway.uploadVideoToBucket(video, infos.courseId(), infos.lessonName());
        }).whenComplete((r, t) -> {
            if(t != null) {
                log.error("Error while uploading the raw video to the bucket {}",t.getMessage());

                handleVideoProcessUpdate(videoId, ProcessStatus.FAILED, t);

                return;
            }

            messagingGateway.sendProcessVideoRequest(
                    new ProcessVideoRequest(
                            videoId,
                            infos.lessonName(),
                            infos.courseId(),
                            video.getOriginalFilename()
                    )
            );
        });

        return "Video processing has started";
    }

    @CacheEvict(value= "videos",key = "#uploadVideoRequest.metadata().courseId()", allEntries = true)
    public void processVideo(ProcessVideoRequest request){
        try {
            validateProcessRequest(request.videoId(), request.lessonName(), request.courseId());

            // tmp file
            File tempFile = videoUtils.createSimpleTempFile(request.videoName());

            // download the raw video to a temp file
            File videoTempFile = s3Gateway.downloadS3RawVideoToTempFile(request.lessonName(), request.videoName(), request.courseId(), tempFile);

            Double duration = videoUtils.getVideoDurationInMinutes(videoTempFile);

            String playlistUrl = s3Gateway.getUrlByPrefix(request.courseId() + "/" + request.lessonName());
            String playbackUrl = s3Gateway.getUrlByPrefix(request.courseId() + "/" + request.lessonName() + "/master.m3u8");
            String thumbnailUrl = s3Gateway.getUrlByPrefix(request.courseId() + "/" + request.lessonName() + "/thumbnail.jpg");

            Video video = new Video(
                    request.videoId(),
                    videoTempFile.getName(),
                    ProcessStatus.PROCESSING,
                    duration,
                    request.videoName(),
                    thumbnailUrl,
                    playlistUrl,
                    playbackUrl,
                    tempFile.length(),
                    "NONE"
            );

            videoGateway.updateVideo(video);

            log.info("Processing video: {} with duration: {} minutes", videoTempFile.getName(), duration);

            CompletableFuture.runAsync(() -> {
                try {
                    processVideoWithMultipleQualities(videoTempFile, request.courseId(), request.lessonName());
                } finally {
                    videoTempFile.delete();
                    tempFile.delete();                                                                                                                                                                                                                                                                                                                                                                               tempFile.delete();
                }
                }, executor).whenComplete((r, t ) -> {
                    if(t != null){

                        log.error("Error processing video {}: {}", videoTempFile.getName(), t.getMessage(), t);

                        this.handleVideoProcessUpdate(request.videoId(),ProcessStatus.FAILED, t);
                        return;
                    }

                    this.handleVideoProcessUpdate(request.videoId(),ProcessStatus.COMPLETED, null);
            });


        } catch (Exception e) {
            handleVideoProcessUpdate(request.videoId(),ProcessStatus.FAILED,e);
        }
    }

    public void deleteVideo(DeleteVideoRequestDto deleteVideoRequestDto){
        if(deleteVideoRequestDto.videoId() == null){
            throw new InvalidVideoDeletionException("Cannot delete a video with a null id");
        }

        videoGateway.delete(deleteVideoRequestDto.videoId());
    }


    private void processVideoWithMultipleQualities(File videoPath, Long courseId, String videoName) {
        try {
            VideoProcessingResult result = videoUtils.convertToMpegDashWithQualities(videoPath.toPath());

            s3Gateway.uploadVideoProcessingResult(result, courseId, videoName);

            log.info("Successfully processed and uploaded video with qualities: {}", videoPath.getName());

        } catch (Exception e) {
            log.error("Error processing video {}: {}", videoName, e.getMessage(), e);
        }
    }

    private void validateProcessRequest(String videoId,String videoName,Long courseId) {
        if (courseId == null || courseId < 0 ||
                videoName == null || videoName.isEmpty() ||
                videoId == null || videoId.isEmpty()) {
            throw new InvalidVideoUploadException("Invalid data for video processing");
        }
    }

    private void handleVideoProcessUpdate(String videoId, ProcessStatus status, @Nullable Throwable e){
        log.info("Updating video status: {}", videoId);

        videoGateway.updateVideoStatus(videoId, status, e != null ? e.getMessage() : "NONE");
    }

}
