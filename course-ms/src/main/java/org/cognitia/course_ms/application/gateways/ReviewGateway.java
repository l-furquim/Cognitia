package org.cognitia.course_ms.application.gateways;

import org.cognitia.course_ms.domain.review.Review;

import java.util.List;

public interface ReviewGateway {

    void create(Review review);
    void delete(Long reviewId);
    Review getFeaturedCourseReview(Long courseId);
    List<Review> getCourseReviews(Long courseId, int start, int end);
    Review findByAuthorAndCourseId(String authorId, Long courseId);

}
