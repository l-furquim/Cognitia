package org.cognitia.course_ms.infra.persistence.repository;

import org.cognitia.course_ms.infra.persistence.UpVoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpVoteJpaRepository extends JpaRepository<UpVoteEntity, Long> {

    @Query("SELECT u FROM UpVoteEntity u WHERE u.authorId = :authorId")
    List<UpVoteEntity> getByAuthorId(@Param("authorId") String authorId);

}
