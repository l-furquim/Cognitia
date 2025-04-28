package org.cognitia.course_ms.application.usecases;

import org.cognitia.course_ms.application.gateways.ReviewGateway;
import org.cognitia.course_ms.domain.review.Review;
import org.cognitia.course_ms.domain.review.dto.CreateReviewRequest;
import org.cognitia.course_ms.domain.review.dto.GetCourseFeaturedReviewResponse;
import org.cognitia.course_ms.domain.review.dto.GetCourseReviewsRequest;
import org.cognitia.course_ms.domain.review.dto.GetCourseReviewsResponse;
import org.cognitia.course_ms.domain.review.exceptions.InvalidCourseFeaturedReviewRequest;
import org.cognitia.course_ms.domain.review.exceptions.InvalidRateRequest;
import org.cognitia.course_ms.domain.review.exceptions.InvalidReviewDeletionRequest;
import org.cognitia.course_ms.domain.review.exceptions.UserAlreadyReviewedThatCourseException;
import org.springframework.stereotype.Service;

@Service
public class ReviewUseCase {

    private final ReviewGateway gateway;

    public ReviewUseCase(ReviewGateway gateway) {
        this.gateway = gateway;
    }

    public void create(CreateReviewRequest request){
        if(
                request.authorId() == null || request.authorId().isEmpty()
                || request.authorName() == null || request.authorName().isEmpty()
                || request.authorProfileUrl() == null || request.authorProfileUrl().isEmpty()
                || request.rate() == null || request.rate() > 5.0
                || request.description() == null || request.description().isEmpty()
                || request.courseId() == null || request.rate() < 0.0
        ){
            throw new InvalidRateRequest("Invalid data for creating a review");
        }

        // validar aqui depois se o curso existe
        // validar aqui depois se o author da review esta matriculado no curso


        var userAlreadyReviwed = gateway.findByAuthorAndCourseId(request.authorId(), request.courseId()) != null;

        if(userAlreadyReviwed){
            throw new UserAlreadyReviewedThatCourseException("A user with this id already reviwed that course");
        }

        var review = Review.builder()
                .authorId(request.authorId())
                .authorName(request.authorName())
                .authorProfileUrl(request.authorProfileUrl())
                .totalLikes(0)
                .description(request.description())
                .rate(request.rate())
                .courseId(request.courseId())
                .build();

        gateway.create(review);
    }

    public void delete(Long reviewId){
        if(reviewId == null){
            throw new InvalidReviewDeletionRequest("Invalid review id for deletion");
        }

        // validar aqui depois se o cara que fez o request que e o dono do review

        gateway.delete(reviewId);
    }

    public GetCourseFeaturedReviewResponse getCourseFeaturedReview(Long courseId){
        if(courseId == null){
            throw new InvalidCourseFeaturedReviewRequest("Course id is null");
        }

        var review = gateway.getFeaturedCourseReview(courseId);

        return new GetCourseFeaturedReviewResponse(review);
    }

    public GetCourseReviewsResponse getCourseReviews(GetCourseReviewsRequest request){
        if(request.courseId() == null){
            throw new InvalidCourseFeaturedReviewRequest("Course id is null");
        }

        var reviews = gateway.getCourseReviews(request.courseId(), request.start(), request.end());

        return new GetCourseReviewsResponse(reviews);
    }

}
