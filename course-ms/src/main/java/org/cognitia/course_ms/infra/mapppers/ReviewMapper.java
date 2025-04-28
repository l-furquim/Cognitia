package org.cognitia.course_ms.infra.mapppers;

import org.cognitia.course_ms.domain.review.Review;
import org.cognitia.course_ms.infra.persistence.ReviewEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReviewMapper {

    public ReviewEntity toEntity(Review review){
        return ReviewEntity.builder()
                .authorId(review.getAuthorId())
                .rate(review.getRate())
                .authorName(review.getAuthorName())
                .authorProfileUrl(review.getAuthorProfileUrl())
                .description(review.getDescription())
                .courseId(review.getCourseId())
                .reviwedAt(LocalDateTime.now())
                .build();
    }

    public Review toDomain(ReviewEntity review){
        return Review.builder()
                .authorId(review.getAuthorId())
                .rate(review.getRate())
                .authorName(review.getAuthorName())
                .authorProfileUrl(review.getAuthorProfileUrl())
                .description(review.getDescription())
                .courseId(review.getCourseId())
                .reviwedAt(review.getReviwedAt())
                .build();
    }

}

