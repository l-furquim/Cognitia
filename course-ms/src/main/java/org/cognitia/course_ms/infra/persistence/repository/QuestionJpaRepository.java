package org.cognitia.course_ms.infra.persistence.repository;

import org.cognitia.course_ms.infra.persistence.QuestionEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Long> {

    @Query("SELECT q FROM QuestionEntity q WHERE q.videoId = :videoId")
    List<QuestionEntity> getByVideoId(@Param("videoId") Long videoId);

    @Query("SELECT SUM(q) FROM QuestionEntity q WHERE q.courseId = :courseId")
    Integer getCourseTotalQuestions(@Param("courseId") Long courseId);

    @Query("SELECT q FROM QuestionEntity q WHERE q.authorId = :authorId")
    List<QuestionEntity> getByAuthorId(@Param("authorId") String authorId);

    @Query("SELECT COUNT(q.id) FROM QuestionEntity q LEFT JOIN UpVoteEntity u ON q.id = u.questionId WHERE q.id = :questionId")
    Long getQuestionTotalVotesById(@Param("questionId") Long questionId);

    @Query(value = """
    SELECT q.*
    FROM question_entity q
    LEFT JOIN up_vote_entity u ON q.id = u.question_id
    WHERE q.video_id = :videoId
    GROUP BY q.id
    ORDER BY COUNT(u.id) DESC NULLS LAST
    LIMIT :limit OFFSET :offset
""", nativeQuery = true)
    List<QuestionEntity> findTopQuestionsByUpVotesAndVideoId(@Param("videoId") Long videoId,@Param("limit") int limit, @Param("offset") int offset);

    @Query(value = """
    SELECT q.*
    FROM question_entity q
    LEFT JOIN up_vote_entity u ON q.id = u.question_id
    WHERE q.course_id = :courseId
    GROUP BY q.id
    ORDER BY COUNT(u.id) DESC
    LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<QuestionEntity> findTopQuestionsByUpVotesAndCourseId(@Param("courseId") Long courseId,@Param("limit") int limit, @Param("offset") int offset);

    @Query("SELECT q FROM QuestionEntity q WHERE q.videoId = :videoId ORDER BY q.questionedAt DESC")
    List<QuestionEntity> findRecentQuestionsByVideoId(@Param("videoId") Long videoId, Pageable pageable);

    @Query("SELECT q FROM QuestionEntity q WHERE q.courseId = :courseId ORDER BY q.questionedAt DESC")
    List<QuestionEntity> findRecentQuestionsByCourseId(@Param("courseId") Long courseId, Pageable pageable);

}
