package org.cognitia.course_ms.infra.persistence.repository;

import org.cognitia.course_ms.infra.persistence.ReviewEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, Long> {

    @Query(value = """
    SELECT r.*
    FROM review_entity r
    LEFT JOIN up_vote_entity l ON r.id = l.review_id
    WHERE r.course_id = :courseId
    GROUP BY r.id
    ORDER BY COUNT(l.id) DESC
    LIMIT 1
""", nativeQuery = true)
    ReviewEntity findTopReview(@Param("courseId") Long courseId);



    @Query("""
            SELECT r
            FROM ReviewEntity r
            WHERE r.courseId = :courseId
            """)
    List<ReviewEntity> findByCourseId(@Param("courseId") Long courseId, Pageable pageable);

    @Query("""
            SELECT r
            FROM ReviewEntity r
            WHERE r.courseId = :courseId
            AND r.authorId = :authorId
            """)
    Optional<ReviewEntity> findByCourseAndAuthorId(@Param("courseId") Long courseId, @Param("authorId") String authorId);

}
