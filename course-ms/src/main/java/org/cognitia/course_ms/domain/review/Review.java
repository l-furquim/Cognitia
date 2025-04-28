package org.cognitia.course_ms.domain.review;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class Review{

    private Double rate;
    private String description;
    private LocalDateTime reviwedAt;
    private Integer totalLikes;
    private String authorId;
    private String authorName;
    private String authorProfileUrl;
    private Long courseId;

}
