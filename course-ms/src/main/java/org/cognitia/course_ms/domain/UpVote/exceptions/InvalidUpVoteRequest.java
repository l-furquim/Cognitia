package org.cognitia.course_ms.domain.UpVote.exceptions;

public class InvalidUpVoteRequest extends RuntimeException {
    public InvalidUpVoteRequest(String message) {
        super(message);
    }
}
