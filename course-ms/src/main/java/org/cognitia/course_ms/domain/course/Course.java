package org.cognitia.course_ms.domain.course;

import lombok.*;
import org.cognitia.course_ms.domain.enums.Topic;
import org.cognitia.course_ms.domain.review.Review;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    private String title;
    private String description;
    private String requirements;
    private String skill;
    private String people;
    private Double price;
    private String thumbUrl;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;
    private String language;
    private List<Topic> topics;
    private Review featuredReview;

}
