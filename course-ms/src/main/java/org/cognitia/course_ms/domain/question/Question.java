package org.cognitia.course_ms.domain.question;


import java.time.LocalDateTime;
import java.util.List;

public record Question(
        String content,
        Long courseId,
        Long videoId,
        Long path,
        LocalDateTime questionedAt,
        String authorId,
        String authorProfileUrl,
        String authorName,
        List<Long> upvotes,
        List<Question> answers,
        Question parent
) {
}
