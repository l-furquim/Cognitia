package org.cognitia.course_ms.application.gateways;
;
import org.cognitia.course_ms.domain.question.Question;
import org.cognitia.course_ms.domain.question.dto.GetCourseQuestionsResponse;

import java.util.List;

public interface QuestionGateway {

    Long create(Question question);
    void addQuestionToBranch(Long newQuestionId, Long parentQuestionId);
    void delete(Long id);
    List<Question> getByVideoId(Long videoId);
    List<Question> getByAuthorId(String authorId);
    Question findById(Long id);
    GetCourseQuestionsResponse getByVideoMostRecents(Long videoId, int start, int end);
    GetCourseQuestionsResponse getByCourseMostRecents(Long courseId,int start, int end);
    GetCourseQuestionsResponse getByVideoMostUpVoted(Long videoId, int start, int end);
    GetCourseQuestionsResponse getByCourseMostUpVoted(Long courseId,int start, int end);

}
