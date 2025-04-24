package org.cognitia.course_ms.domain.model;

import java.time.LocalDateTime;

public record UpVote(
        LocalDateTime upvotedAt,
        String authorId
) {
}
