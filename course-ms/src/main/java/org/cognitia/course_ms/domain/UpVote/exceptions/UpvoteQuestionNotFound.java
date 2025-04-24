package org.cognitia.course_ms.domain.UpVote.exceptions;

public class UpvoteQuestionNotFound extends RuntimeException {
    public UpvoteQuestionNotFound(String message) {
        super(message);
    }
}
