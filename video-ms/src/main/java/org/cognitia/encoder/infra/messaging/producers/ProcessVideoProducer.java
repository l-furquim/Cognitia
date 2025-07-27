package org.cognitia.encoder.infra.messaging.producers;

import lombok.extern.slf4j.Slf4j;
import org.cognitia.encoder.application.gateways.MessagingGateway;
import org.cognitia.encoder.infra.dto.video.ProcessVideoRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProcessVideoProducer implements MessagingGateway {

    private final KafkaTemplate<String, ProcessVideoRequest> template;

    public ProcessVideoProducer(KafkaTemplate<String, ProcessVideoRequest> template) {
        this.template = template;
    }

    @Override
    public void sendProcessVideoRequest(ProcessVideoRequest request) {
        log.info("Sending process video message: {}", request);

        template.send("video.encode", request);
    }
}
