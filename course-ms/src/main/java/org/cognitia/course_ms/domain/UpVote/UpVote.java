package org.cognitia.course_ms.domain.UpVote;

import java.time.LocalDateTime;

public record UpVote(
        LocalDateTime upvotedAt,
        String authorId,
        Long questionId
) {
}
