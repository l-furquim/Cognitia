package org.cognitia.course_ms.application.gateways;


import org.cognitia.course_ms.domain.UpVote.UpVote;

import java.util.List;

public interface UpVoteGateway {

    Long create(UpVote request);
    void delete(Long upvoteId);
    List<UpVote> getByAuthor(String authorId);
    UpVote findById(Long id);
    UpVote findByQuestionAndAuthorId(String authorId, Long questionId);

}
