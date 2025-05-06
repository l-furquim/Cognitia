package org.cognitia.course_ms.domain.course.dto;

import org.cognitia.course_ms.domain.enums.Topic;
import org.cognitia.course_ms.domain.path.dto.GetPathDataByCourseResponse;
import org.cognitia.course_ms.domain.review.Review;

import java.time.LocalDateTime;
import java.util.List;

public record GetCourseDataResponse(
        String title,
        String description,
        String requirements,
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
        List<GetPathDataByCourseResponse> paths,
        List<Review> reviews,
        Review featuredReview,
        Boolean userRegistered,
        Boolean userLogged
) {
}
