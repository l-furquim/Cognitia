package org.cognitia.course_ms.domain.model;

import org.cognitia.course_ms.domain.enums.Topic;
import org.cognitia.course_ms.domain.path.Path;
import org.cognitia.course_ms.domain.review.Review;

import java.time.LocalDateTime;
import java.util.List;

public record Course(
        String title,
        String description,
        String skill,
        String people,
        Double price,
        Double rate,
        String thumbUrl,
        LocalDateTime createdAt,
        LocalDateTime lastUpdate,
        String language,
        Double totalHours,
        Double totalRate,
        Double totalStudents,
        List<Topic> topics,
        List<Review> reviews,
        Review featuredReview,
        List<Path> allowedToWatch
) {
}
