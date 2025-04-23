package org.cognitia.video_ms.infra.controllers.impl;

import lombok.extern.slf4j.Slf4j;
import org.cognitia.video_ms.application.usecases.VideoUseCase;
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

    @GetMapping("{courseId}")
    public ResponseEntity<GetCourseVideosResponse> getCourseVideos(@PathVariable("courseId")Long courseId){
        
    }



}
