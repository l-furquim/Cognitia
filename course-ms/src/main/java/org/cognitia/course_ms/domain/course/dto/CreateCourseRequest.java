package org.cognitia.course_ms.domain.course.dto;

import org.cognitia.course_ms.domain.enums.Topic;
import org.cognitia.course_ms.domain.review.Review;

import java.time.LocalDateTime;
import java.util.List;

public record CreateCourseRequest(
        String title,
        String description,
        String requirements,
        String skill,
        String people,
        Double price,
        String thumbUrl,
        LocalDateTime createdAt,
        LocalDateTime lastUpdate,
        String language,
        List<Topic> topics,
        Review featuredReview
) {
}
