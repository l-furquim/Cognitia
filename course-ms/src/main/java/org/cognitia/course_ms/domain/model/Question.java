package org.cognitia.course_ms.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public record Question(
        String content,
        LocalDateTime questionedAt,
        String authorId,
        String authorProfileUrl,
        String authorName,
        List<UpVote> upvotes,
        List<Question> ineQuestions
) {
}
