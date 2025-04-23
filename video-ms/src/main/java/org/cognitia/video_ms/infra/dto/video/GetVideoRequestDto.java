package org.cognitia.video_ms.infra.dto.video;

public record GetVideoRequestDto(
        Long courseId,
        Long path
){
}
