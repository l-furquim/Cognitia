package org.cognitia.video_ms.infra.persistence.repository;

import org.cognitia.video_ms.infra.persistence.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoJpaRepository extends JpaRepository<VideoEntity, Long> {

    @Query("SELECT v FROM VideoEntity v WHERE v.courseId = :id")
    List<VideoEntity> getByCourseId(@Param("id") Long id);

}
