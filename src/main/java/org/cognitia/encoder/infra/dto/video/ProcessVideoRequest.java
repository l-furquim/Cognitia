package org.cognitia.encoder.infra.dto.video;

public record ProcessVideoRequest(
        String videoId,
        String lessonName,
        Long courseId,
        String videoName
) {
}
