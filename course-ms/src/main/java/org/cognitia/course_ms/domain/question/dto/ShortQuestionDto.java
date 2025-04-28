package org.cognitia.course_ms.domain.question.dto;

public record ShortQuestionDto(
        Boolean recent,
        Boolean mostUpVoted,
        Boolean recommended
) {
}
