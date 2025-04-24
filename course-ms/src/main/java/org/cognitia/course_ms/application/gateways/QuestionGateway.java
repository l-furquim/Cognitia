package org.cognitia.course_ms.application.gateways;
;
import org.cognitia.course_ms.domain.question.Question;

import java.util.List;

public interface QuestionGateway {

//    void addUpVote(Long upVoteId, Long questionId);
//    void removeUpVote(Long upVoteId, Long questionId);
    Long create(Question question);
    void addQuestionToBranch(Long newQuestionId, Long parentQuestionId);
    void delete(Long id);
    List<Question> getByVideoId(Long videoId);
    Integer getCourseTotalQuestions(Long courseId);
    List<Question> getByAuthorId(String authorId);
    Question findById(Long id);


}
