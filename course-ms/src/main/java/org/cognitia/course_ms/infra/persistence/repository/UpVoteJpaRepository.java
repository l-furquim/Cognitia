package org.cognitia.course_ms.infra.persistence.repository;

import org.cognitia.course_ms.infra.persistence.UpVoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UpVoteJpaRepository extends JpaRepository<UpVoteEntity, Long> {

    @Query("SELECT u FROM UpVoteEntity u WHERE u.authorId = :authorId")
    List<UpVoteEntity> getByAuthorId(@Param("authorId") String authorId);

    @Query("SELECT u FROM UpVoteEntity u WHERE u.authorId = :authorId AND u.questionId = :questionId")
    Optional<UpVoteEntity> getByAuthorAndQuestionId(@Param("authorId") String authorId, @Param("questionId") Long questionId);

}
