package org.cognitia.encoder.infra.controllers.impl;

import lombok.extern.slf4j.Slf4j;
import org.cognitia.encoder.application.usecases.VideoUseCase;
import org.cognitia.encoder.infra.controllers.VideoController;
import org.cognitia.encoder.infra.dto.video.*;
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
    public ResponseEntity<String> uploadVideo(
            @RequestPart("infos") UploadVideoInfos infos,
            @RequestPart("video") MultipartFile video
    ){
        log.info("Chegou aqui");

        String message = videoUseCase.uploadVideo(video, infos);

        return ResponseEntity.status(201).body(message);
    }

    @DeleteMapping("/video/{videoId}")
    public ResponseEntity<Void> deleteVideo(@PathVariable("videoId") String videoId){
        videoUseCase.deleteVideo(new DeleteVideoRequestDto(videoId));

        return ResponseEntity.status(204).build();
    }

}
