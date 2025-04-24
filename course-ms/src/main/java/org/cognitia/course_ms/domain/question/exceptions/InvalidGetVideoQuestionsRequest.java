package org.cognitia.course_ms.domain.question.exceptions;

public class InvalidGetVideoQuestionsRequest extends RuntimeException {
    public InvalidGetVideoQuestionsRequest(String message) {
        super(message);
    }
}
