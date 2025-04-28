package org.cognitia.course_ms.infra.gateways;

import org.cognitia.course_ms.application.gateways.ReviewGateway;
import org.cognitia.course_ms.domain.review.Review;
import org.cognitia.course_ms.infra.mapppers.ReviewMapper;
import org.cognitia.course_ms.infra.persistence.repository.ReviewJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewRepositoryGateway implements ReviewGateway {

    private final ReviewJpaRepository repository;
    private final ReviewMapper mapper;

    public ReviewRepositoryGateway(ReviewJpaRepository repository, ReviewMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void create(Review review) {
        var reviewEntity = mapper.toEntity(review);
        repository.save(reviewEntity);
    }

    @Override
    public void delete(Long reviewId) {
        var review = repository.findById(reviewId);

        if(review.isPresent()) repository.delete(review.get());
    }

    @Override
    public Review getFeaturedCourseReview(Long courseId) {
        var reviewEntity = repository.findTopReview(courseId);

        if(reviewEntity != null){
            return mapper.toDomain(reviewEntity);
        }

        return null;
    }

    @Override
    public List<Review> getCourseReviews(Long courseId, int start, int end) {
        return repository.findByCourseId(courseId, PageRequest.of(start,end))
                .stream().map(
                        r -> mapper.toDomain(r)
                ).toList();
    }

    @Override
    public Review findByAuthorAndCourseId(String authorId, Long courseId) {
        var review = repository.findByCourseAndAuthorId(courseId, authorId);

        if(review.isPresent()) return mapper.toDomain(review.get());

        return null;
    }
}
