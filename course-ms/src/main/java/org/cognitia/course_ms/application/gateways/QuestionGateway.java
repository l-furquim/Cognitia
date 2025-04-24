package org.cognitia.course_ms.application.gateways;

import org.cognitia.course_ms.domain.model.UpVote;

public interface QuestionGateway {

    void addUpVote(UpVote upVote);
    void removeUpVote(UpVote upVote);

}
