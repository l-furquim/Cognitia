package org.cognitia.encoder.infra.dto.video;

public record GetVideoRequestDto(
        Long courseId,
        Long path
){
}
