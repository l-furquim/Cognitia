package org.cognitia.course_ms.domain.question.dto;

public record GetCourseQuestionsRequest (
        Long courseId,
        Long videoId,
        ShortQuestionDto shortOption,
        Integer pageStart,
        Integer pageEnd
){
}
