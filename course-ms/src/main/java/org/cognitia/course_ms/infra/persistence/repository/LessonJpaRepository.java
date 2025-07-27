package org.cognitia.course_ms.infra.persistence.repository;

import org.cognitia.course_ms.infra.persistence.LessonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonJpaRepository extends JpaRepository<LessonEntity, Long> {

    @Query("SELECT v FROM LessonEntity v WHERE v.courseId = :id")
    List<LessonEntity> getByCourseId(@Param("id") Long id);


    @Query("SELECT v FROM LessonEntity v WHERE v.path = :path")
    List<LessonEntity> getByPath(@Param("path") Long path);
}
