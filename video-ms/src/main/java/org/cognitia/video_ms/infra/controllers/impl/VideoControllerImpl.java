package org.cognitia.video_ms.infra.controllers.impl;

import org.cognitia.video_ms.application.usecases.VideoUseCase;
import org.cognitia.video_ms.infra.controllers.VideoController;
import org.cognitia.video_ms.infra.dto.video.UploadVideoRequest;
import org.cognitia.video_ms.infra.dto.video.VideoMetadataDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("v1/api/videos")
public class VideoControllerImpl implements VideoController {

    private final VideoUseCase videoUseCase;

    public VideoControllerImpl(VideoUseCase videoUseCase) {
        this.videoUseCase = videoUseCase;
    }

    @PostMapping
    public ResponseEntity<String> uploadVideo(
            @RequestPart VideoMetadataDto data,
            @RequestPart MultipartFile video
    ){
        var url = videoUseCase.uploadVideo(new UploadVideoRequest(data, video));

        return ResponseEntity.status(204).body(url);
    }


}
