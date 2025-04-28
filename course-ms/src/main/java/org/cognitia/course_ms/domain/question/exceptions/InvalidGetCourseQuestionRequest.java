package org.cognitia.course_ms.domain.question.exceptions;

public class InvalidGetCourseQuestionRequest extends RuntimeException {
    public InvalidGetCourseQuestionRequest(String message) {
        super(message);
    }
}
