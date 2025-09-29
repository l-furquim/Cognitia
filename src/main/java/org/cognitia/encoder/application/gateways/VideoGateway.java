package org.cognitia.encoder.application.gateways;

import org.cognitia.encoder.domain.enums.ProcessStatus;
import org.cognitia.encoder.domain.model.Video;

public interface VideoGateway {

    void upload(Video video);
    void updateVideo(Video video);
    void updateVideoStatus(String videoId, ProcessStatus status, String failedReason);
    void delete(String videoId);
}
