package org.cognitia.video_ms.infra.controllers.impl;

import lombok.extern.slf4j.Slf4j;
import org.cognitia.video_ms.application.usecases.VideoUseCase;
import org.cognitia.video_ms.domain.model.Video;
import org.cognitia.video_ms.infra.controllers.VideoController;
import org.cognitia.video_ms.infra.dto.video.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("v1/api/videos")
public class VideoControllerImpl implements VideoController {

    private final VideoUseCase videoUseCase;

    public VideoControllerImpl(VideoUseCase videoUseCase) {
        this.videoUseCase = videoUseCase;
    }

    @PostMapping
    public ResponseEntity<UploadVideoResponse> uploadVideo(
            @RequestPart VideoMetadataDto data,
            @RequestPart MultipartFile video
    ){
        var response = videoUseCase.uploadVideo(new UploadVideoRequest(data, video));

        return ResponseEntity.status(201).body(response);
    }


    @PostMapping("/thumb")
    public ResponseEntity<UploadVideoThumbResponse> uploadVideoThumb(
            @RequestPart UploadVideoThumbMetadataDto data,
            @RequestPart MultipartFile image
    ){
        var url = videoUseCase.uploadVideoThumb(new UploadVideoThumbRequest(data, image));

        return ResponseEntity.ok().body(url);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<GetCourseVideosResponse> getCourseVideos(@PathVariable("courseId")Long courseId){
        var videos = videoUseCase.getCourseVideos(courseId);

        return ResponseEntity.ok().body(videos);
    }

    @DeleteMapping("/video/{videoId}")
    public ResponseEntity<Void> deleteVideo(@PathVariable("videoId") Long videoId){
        videoUseCase.deleteVideo(new DeleteVideoRequestDto(videoId));

        return ResponseEntity.status(204).build();
    }

    @PutMapping
    public ResponseEntity<Video> updateVideoMetadata(@RequestBody UpdateVideoMetadataRequest request){
        var video = videoUseCase.updateVideo(request);

        return ResponseEntity.ok().body(video);
    }


}
