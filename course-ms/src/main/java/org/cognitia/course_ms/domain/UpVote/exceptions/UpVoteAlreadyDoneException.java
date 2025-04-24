package org.cognitia.course_ms.domain.UpVote.exceptions;

public class UpVoteAlreadyDoneException extends RuntimeException {
    public UpVoteAlreadyDoneException(String message) {
        super(message);
    }
}
