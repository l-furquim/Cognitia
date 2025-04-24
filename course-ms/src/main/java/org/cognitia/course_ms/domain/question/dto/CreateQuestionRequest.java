package org.cognitia.course_ms.domain.question.dto;


import org.cognitia.course_ms.domain.question.Question;

public record CreateQuestionRequest(
        String content,
        Long courseId,
        Long videoId,
        Long path,
        String authorId,
        String authorProfileUrl,
        String authorName,
        Long parentId
) {
}
