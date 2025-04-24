package org.cognitia.course_ms.domain.question.exceptions;

public class InvalidQuestionCreateRequest extends RuntimeException {
    public InvalidQuestionCreateRequest(String message) {
        super(message);
    }
}
