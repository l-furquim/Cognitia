package org.cognitia.encoder.application.gateways;

import org.cognitia.encoder.infra.dto.video.ProcessVideoRequest;

public interface MessagingGateway {

    void sendProcessVideoRequest(ProcessVideoRequest request);

}
