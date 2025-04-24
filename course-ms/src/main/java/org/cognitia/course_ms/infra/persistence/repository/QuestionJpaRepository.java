package org.cognitia.course_ms.infra.persistence.repository;

import org.cognitia.course_ms.infra.persistence.QuestionEntity;
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

}
