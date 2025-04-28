package org.cognitia.course_ms.domain.question.dto;

import org.cognitia.course_ms.domain.question.Question;

import java.util.List;

public record GetCourseQuestionsResponse(
        List<Question> questions
) {
}
