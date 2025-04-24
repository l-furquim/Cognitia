package org.cognitia.course_ms.domain.question.dto;


public record CreateQuestionRequest(
        String content,
        Long courseId,
        Long videoId,
        Long path,
        String authorId,
        String authorProfileUrl,
        String authorName
) {
}
