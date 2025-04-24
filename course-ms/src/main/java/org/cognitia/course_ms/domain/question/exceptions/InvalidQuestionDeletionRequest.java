package org.cognitia.course_ms.domain.question.exceptions;

public class InvalidQuestionDeletionRequest extends RuntimeException {
    public InvalidQuestionDeletionRequest(String message) {
        super(message);
    }
}
