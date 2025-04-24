package org.cognitia.course_ms.domain.UpVote.exceptions;

public class InvalidGetUpvotesByAuthorRequest extends RuntimeException {
    public InvalidGetUpvotesByAuthorRequest(String message) {
        super(message);
    }
}
