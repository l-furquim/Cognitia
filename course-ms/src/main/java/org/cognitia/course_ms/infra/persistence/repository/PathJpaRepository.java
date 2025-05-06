package org.cognitia.course_ms.infra.persistence.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.cognitia.course_ms.infra.persistence.PathEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PathJpaRepository extends JpaRepository<PathEntity, Long> {


    @Query("SELECT p FROM PathEntity p WHERE p.courseId = :courseId")
    List<PathEntity> findByCourseId(@Param("courseId") Long courseId);

}
