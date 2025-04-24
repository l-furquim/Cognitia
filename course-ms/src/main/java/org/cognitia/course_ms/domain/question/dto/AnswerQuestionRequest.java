package org.cognitia.course_ms.domain.question.dto;

public record AnswerQuestionRequest(
        CreateQuestionRequest createRequest,
        Long question
) {
}
