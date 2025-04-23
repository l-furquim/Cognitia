package org.cognitia.video_ms.infra.persistence.repository;

import org.cognitia.video_ms.infra.persistence.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoJpaRepository extends JpaRepository<VideoEntity, Long> {
}
