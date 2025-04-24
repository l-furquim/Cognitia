package org.cognitia.course_ms.domain.UpVote.exceptions;

public class InvalidUpVoteDeleteRequest extends RuntimeException {
    public InvalidUpVoteDeleteRequest(String message) {
        super(message);
    }
}
