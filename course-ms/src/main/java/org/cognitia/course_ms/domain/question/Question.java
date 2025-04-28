package org.cognitia.course_ms.domain.question;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    private String content;
    private Long courseId;
    private Long videoId;
    private Long path;
    private LocalDateTime questionedAt;
    private String authorId;
    private String authorProfileUrl;
    private String authorName;
    private List<Question> answers;
    private Question parent;
    private Long upvotes;
}
