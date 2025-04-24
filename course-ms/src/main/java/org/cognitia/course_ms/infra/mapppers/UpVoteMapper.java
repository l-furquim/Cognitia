package org.cognitia.course_ms.infra.mapppers;

import org.cognitia.course_ms.domain.UpVote.UpVote;
import org.cognitia.course_ms.infra.persistence.UpVoteEntity;
import org.springframework.stereotype.Component;

@Component
public class UpVoteMapper {

    public UpVoteEntity toEntity(UpVote upVote){
        return UpVoteEntity.builder()
                .upVotedAt(upVote.upvotedAt())
                .authorId(upVote.authorId())
                .build();
    }

    public UpVote toDomain(UpVoteEntity upVote){
        return new UpVote(
                upVote.getUpVotedAt(),
                upVote.getAuthorId(),
                upVote.getQuestionId()
        );
    }

}
