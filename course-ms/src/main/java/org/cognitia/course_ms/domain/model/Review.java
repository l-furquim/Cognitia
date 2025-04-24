package org.cognitia.course_ms.domain.model;

import java.time.LocalDateTime;

public record Review(
        Double rate,
        String description,
        LocalDateTime reviwedAt,
        Integer totalLikes,
        String authorId,
        String authorName,
        String authorProfileUrl
) {
}
