package org.cognitia.course_ms.domain.review.dto;

import org.cognitia.course_ms.domain.review.Review;

import java.util.List;

public record GetCourseReviewsResponse(
        List<Review> reviews
) {

}
