package org.cognitia.course_ms.domain.question.exceptions;

public class InvalidAnswerQuestionRequest extends RuntimeException {
    public InvalidAnswerQuestionRequest(String message) {
        super(message);
    }
}
