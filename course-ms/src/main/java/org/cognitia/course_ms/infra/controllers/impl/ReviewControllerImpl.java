package org.cognitia.course_ms.infra.controllers.impl;

import org.cognitia.course_ms.application.usecases.ReviewUseCase;
import org.cognitia.course_ms.domain.review.dto.CreateReviewRequest;
import org.cognitia.course_ms.domain.review.dto.GetCourseFeaturedReviewResponse;
import org.cognitia.course_ms.domain.review.dto.GetCourseReviewsRequest;
import org.cognitia.course_ms.domain.review.dto.GetCourseReviewsResponse;
import org.cognitia.course_ms.infra.controllers.ReviewController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("v1/api/reviews")
@RestController
public class ReviewControllerImpl implements ReviewController {

    private final ReviewUseCase useCase;

    public ReviewControllerImpl(ReviewUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateReviewRequest request){
        useCase.create(request);

        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("{reviewId}")
    public ResponseEntity<Void> delete(@PathVariable("reviewId") Long reviewId){
        useCase.delete(reviewId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/course/all")
    public ResponseEntity<GetCourseReviewsResponse> getByCourse(@RequestBody GetCourseReviewsRequest request){
        var reviews = useCase.getCourseReviews(request);

        return ResponseEntity.ok().body(reviews);
    }

    @GetMapping("/course/featured/{courseId}")
    public ResponseEntity<GetCourseFeaturedReviewResponse> getCourseFeatured(@PathVariable("courseId") Long courseId){
        var review = useCase.getCourseFeaturedReview(courseId);

        if(review == null){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(review);
    }


}
