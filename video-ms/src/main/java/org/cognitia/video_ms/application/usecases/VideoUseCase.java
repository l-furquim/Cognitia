package org.cognitia.video_ms.application.usecases;

import org.cognitia.video_ms.application.gateways.S3Gateway;
import org.cognitia.video_ms.application.gateways.VideoGateway;
import org.cognitia.video_ms.domain.model.Video;
import org.cognitia.video_ms.domain.exceptions.*;
import org.cognitia.video_ms.infra.dto.video.*;
import org.cognitia.video_ms.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class VideoUseCase {

    private static final Logger log = LoggerFactory.getLogger(VideoUseCase.class);
    private final VideoGateway videoGateway;
    private final S3Gateway s3Gateway;
    private final FileUtils fileUtils;

    public VideoUseCase
            (
            VideoGateway videoGateway,
            S3Gateway s3Gateway,
            FileUtils fileUtils
    ){
        this.videoGateway = videoGateway;
        this.s3Gateway = s3Gateway;
        this.fileUtils = fileUtils;
    }
    @CacheEvict(key = "#uploadVideoRequest.metadata().courseId()", allEntries = true)
    public UploadVideoResponse uploadVideo(UploadVideoRequest uploadVideoRequest){
        VideoMetadataDto metadata = uploadVideoRequest.metadata();

        if(
                metadata.courseId() == null || metadata.courseId() < 0 ||
                metadata.title() == null || metadata.title().isEmpty() ||
                metadata.description() == null || metadata.description().isEmpty() ||
                metadata.path() == null || metadata.skill() == null || metadata.skill().isEmpty()
        ){
            throw new InvalidVideoUploadException("Invalid data for video upload");
        }

//        if(!uploadVideoRequest.video().getContentType().equals(".mp4")){
//            throw new InvalidVideoContentTypeException("Invalid video extension, we only support mp4");
//        }

        var videoPath = fileUtils.convertToFile(uploadVideoRequest.video());

        Double duration = fileUtils.getVideoDurationInMinutes(videoPath);


        log.info("Video path atual " + videoPath);
        log.info("Duracao atual " + duration);

        new Thread(() -> {
            var mpegPath = fileUtils.convertToMpegDash(videoPath.toPath());

            log.info("Video converted to mpeg dash " + mpegPath);

            s3Gateway.uploadVideoToBucket(mpegPath, uploadVideoRequest.metadata().courseId(), uploadVideoRequest.video().getOriginalFilename());
        }).start();

        var url = s3Gateway.getUrlByPrefix(metadata.courseId() + "/" + uploadVideoRequest.video().getOriginalFilename());

        var video = new Video(
                metadata.title(),
                metadata.description(),
                uploadVideoRequest.video().getOriginalFilename(),
                metadata.path(),
                metadata.skill(),
                duration,
                url,
                null,
                metadata.courseId()
        );

        new Thread(() -> {
            videoGateway.upload(video);
        }).start();

        return new UploadVideoResponse(video);
    }

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



}
