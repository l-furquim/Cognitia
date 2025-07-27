package org.cognitia.encoder.domain.model;

import org.cognitia.encoder.domain.enums.ProcessStatus;


public record Video(
        String id,
        String name,
        ProcessStatus status,
        Double durationInMinutes,
        String extension,
        String thumbUrl,
        String playlistsUrl,
        String playbackUrl,
        double originalFileSize,
        String failedReason
) {
}
