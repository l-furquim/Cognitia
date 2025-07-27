package org.cognitia.course_ms.domain.lesson.dto;

public record GetLessonRequestDto(
        Long courseId,
        Long path
){
}
