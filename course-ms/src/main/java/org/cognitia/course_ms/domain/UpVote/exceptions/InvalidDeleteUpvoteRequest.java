package org.cognitia.course_ms.domain.UpVote.exceptions;

public class InvalidDeleteUpvoteRequest extends RuntimeException {
    public InvalidDeleteUpvoteRequest(String message) {
        super(message);
    }
}
