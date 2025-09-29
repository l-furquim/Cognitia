package org.cognitia.encoder.infra.messaging.listeners;

import lombok.extern.slf4j.Slf4j;
import org.cognitia.encoder.application.usecases.VideoUseCase;
import org.cognitia.encoder.infra.dto.video.ProcessVideoRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProcessVideoListener {

    private final VideoUseCase usecase;

    public ProcessVideoListener(VideoUseCase usecase) {
        this.usecase = usecase;
    }

    @KafkaListener(topics = "video.encode", groupId = "encoder")
    public void lister(ProcessVideoRequest message){
        try {
            log.info("Video encode request received: {}", message);

            usecase.processVideo(message);
        } catch (Exception e) {
            log.error("Error while consuming the fraud.check.completed message", e);
        }
    }

}
