package org.cognitia.video_ms.application.usecases;

import org.cognitia.video_ms.application.gateways.S3Gateway;
import org.cognitia.video_ms.application.gateways.VideoGateway;
import org.cognitia.video_ms.domain.model.Video;
import org.cognitia.video_ms.domain.exceptions.*;
import org.cognitia.video_ms.infra.dto.video.*;
import org.cognitia.video_ms.utils.VideoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class VideoUseCase {

    private static final Logger log = LoggerFactory.getLogger(VideoUseCase.class);
    private final VideoGateway videoGateway;
    private final S3Gateway s3Gateway;
    private final VideoUtils videoUtils;
    private static final String TOPIC = "path.add";

    public VideoUseCase
            (
            VideoGateway videoGateway,
            S3Gateway s3Gateway,
            VideoUtils videoUtils
            ){
        this.videoGateway = videoGateway;
        this.s3Gateway = s3Gateway;
        this.videoUtils = videoUtils;
    }
    @CacheEvict(value= "videos",key = "#uploadVideoRequest.metadata().courseId()", allEntries = true)
    public UploadVideoResponse uploadVideo(UploadVideoRequest uploadVideoRequest){
        VideoMetadataDto metadata = uploadVideoRequest.metadata();

        validateUploadRequest(metadata);

        // tmp file
        File videoPath = videoUtils.convertToTempFile(uploadVideoRequest.video());
        Double duration = videoUtils.getVideoDurationInMinutes(videoPath);

        String originalFilename = uploadVideoRequest.video().getOriginalFilename();
        String videoName = sanitizeVideoName(originalFilename);

        log.info("Processing video: {} with duration: {} minutes", videoName, duration);

        CompletableFuture.runAsync(() -> {
            try {
                processVideoWithMultipleQualities(videoPath, metadata.courseId(), videoName);
            } catch (Exception e) {
                log.error("Error processing video {}: {}", videoName, e.getMessage(), e);
            }
        });

        String videoUrl = s3Gateway.getUrlByPrefix(metadata.courseId() + "/" + videoName + "/master.m3u8");
        String thumbnailUrl = s3Gateway.getUrlByPrefix(metadata.courseId() + "/" + videoName + "/thumbnail.jpg");

        Video video = new Video(
                metadata.title(),
                metadata.description(),
                videoName,
                metadata.path(),
                metadata.skill(),
                duration,
                videoUrl,
                thumbnailUrl,
                metadata.courseId()
        );

        Video savedVideo = videoGateway.upload(video);

        return new UploadVideoResponse(savedVideo);
    }
    @CachePut(value = "videos", key = "metadata.videoId()")
    public UploadVideoThumbResponse uploadVideoThumb(UploadVideoThumbRequest request){
        var metadata = request.metadata();
        var originalName = request.image().getOriginalFilename();

        if(metadata.videoId() == null || metadata.userId() == null){
            throw new InvalidVideoThumbUploadException("Invalid data while uploading the video thumb");
        }

        var video = videoGateway.findById(metadata.videoId());

        if(video == null){
            throw new VideoNotFoundException("Could not found the video while uploading his thumb");
        }

        var thumbUrl = s3Gateway.getUrlByPrefix(video.courseId() + "/" + video.originalName() + "/" + request.image().getOriginalFilename());

        new Thread(() -> {
            s3Gateway.uploadThumbToBucket(
                    request.image(),
                    video.courseId(),
                    video.originalName()
            );

            videoGateway.uploadThumb(metadata.videoId(), thumbUrl);

        }).start();

        return new UploadVideoThumbResponse(thumbUrl);
    }

    @Cacheable(value="videos", key = "#courseId")
    public GetCourseVideosResponse getCourseVideos(Long courseId){
        if(courseId == null){
            throw new InvalidCurseVideosRequestException("Invalid curse id for searching the videos");
        }

        var videos = videoGateway.getByCourseId(courseId);

        return new GetCourseVideosResponse(videos);
    }

    public void deleteVideo(DeleteVideoRequestDto deleteVideoRequestDto){
        if(deleteVideoRequestDto.videoId() == null){
            throw new InvalidVideoDeletionException("Cannot delete a video with a null id");
        }

        videoGateway.delete(deleteVideoRequestDto);
    }
    public Video updateVideo(UpdateVideoMetadataRequest request){
        if(request.videoId() == null){
            throw new InvalidVideoUpdateException("Video id is null, cannot update");
        }

        return videoGateway.update(request);
    }

    public List<Video> getPathVideos(Long pathId){
        if(pathId == null){
            throw new InvalidGetPathVideosRequest("Path id is null, cannot get videos");
        }

        return videoGateway.getByPath(pathId);
    }

    private String sanitizeVideoName(String originalFilename) {
        if (originalFilename == null) {
            return "video_" + System.currentTimeMillis();
        }

        // Remove extension and sanitize
        String baseName = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
        return baseName.replaceAll("[^a-zA-Z0-9._-]", "_").toLowerCase();
    }

    private void processVideoWithMultipleQualities(File videoPath, Long courseId, String videoName) {
        try {
            VideoProcessingResult result = videoUtils.convertToMpegDashWithQualities(videoPath.toPath());

            s3Gateway.uploadVideoProcessingResult(result, courseId, videoName);

            log.info("Successfully processed and uploaded video with qualities: {}", videoName);

        } catch (Exception e) {
            log.error("Error processing video {}: {}", videoName, e.getMessage(), e);
        }
    }

    private void validateUploadRequest(VideoMetadataDto metadata) {
        if (metadata.courseId() == null || metadata.courseId() < 0 ||
                metadata.title() == null || metadata.title().isEmpty() ||
                metadata.description() == null || metadata.description().isEmpty() ||
                metadata.path() == null || metadata.skill() == null || metadata.skill().isEmpty()) {

            throw new InvalidVideoUploadException("Invalid data for video upload");
        }
    }

}
