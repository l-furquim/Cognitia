package org.cognitia.course_ms.infra.gateways;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.cognitia.course_ms.application.gateways.QuestionGateway;
import org.cognitia.course_ms.domain.question.Question;
import org.cognitia.course_ms.domain.question.dto.GetCourseQuestionsResponse;
import org.cognitia.course_ms.infra.mapppers.QuestionMapper;
import org.cognitia.course_ms.infra.persistence.repository.QuestionJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class QuestionRepositoryGateway implements QuestionGateway {

    private final QuestionJpaRepository repository;
    private final QuestionMapper questionMapper;

    public QuestionRepositoryGateway(QuestionJpaRepository repository, QuestionMapper questionMapper) {
        this.repository = repository;
        this.questionMapper = questionMapper;
    }

    @Override
    public Long create(Question question) {
        var questionEntity = questionMapper.toEntity(question);

        return repository.save(questionEntity).getId();
    }

    @Transactional
    @Override
    public void addQuestionToBranch(Long newQuestionId, Long parentQuestionId) {
        var newQuestion = repository.findById(newQuestionId);

        var parentQuestion = repository.findById(parentQuestionId);

        parentQuestion.get().addNewQuestion(newQuestion.get());
        newQuestion.get().setParent(parentQuestion.get());

        repository.save(newQuestion.get());
        repository.save(parentQuestion.get());
    }

    @Override
    public void delete(Long id) {
        var question = repository.findById(id);

        if(question.isPresent()) repository.delete(question.get());
    }

    @Override
    public List<Question> getByVideoId(Long videoId) {
        return repository.getByVideoId(videoId).stream().map(
                q -> questionMapper.toDomain(q, false)
        ).toList();
    }

    @Override
    public List<Question> getByAuthorId(String authorId) {
        return repository.getByAuthorId(authorId).stream().map(
                q -> questionMapper.toDomain(q, true)
        ).toList();
    }

    @Override
    public Question findById(Long id) {
        var q = repository.findById(id);

        log.info("Question: {}", q.toString());

        if(q.isPresent()) return questionMapper.toSimpleDomain(q.get());

        return null;
    }

    @Override
    public GetCourseQuestionsResponse getByVideoMostRecents(Long videoId, int start, int end) {
        var questions = repository.findRecentQuestionsByVideoId(videoId,PageRequest.of(start, end));

        List<Question> qs = new ArrayList<>();

        questions.forEach(
                q -> {

                    Question questionDomain = questionMapper.toSimpleDomain(q);
                    questionDomain.setUpvotes(repository.getQuestionTotalVotesById(q.getId()));

                    qs.add(questionDomain);

                }
        );

        return new GetCourseQuestionsResponse(qs);
    }

    @Override
    public GetCourseQuestionsResponse getByCourseMostRecents(Long courseId,int start, int end) {
        var questions = repository.findRecentQuestionsByCourseId(courseId,PageRequest.of(start, end));

        List<Question> qs = new ArrayList<>();

        questions.forEach(
                q -> {

                    Question questionDomain = questionMapper.toSimpleDomain(q);
                    questionDomain.setUpvotes(repository.getQuestionTotalVotesById(q.getId()));

                    qs.add(questionDomain);

                }
        );

        return new GetCourseQuestionsResponse(qs);
    }

    @Override
    public GetCourseQuestionsResponse getByVideoMostUpVoted(Long videoId,int start, int end) {
        var questions = repository.findTopQuestionsByUpVotesAndVideoId(videoId, start, end);

        log.info("Questions: {}", questions.toString());

        List<Question> qs = new ArrayList<>();

        questions.forEach(
                q -> {

                    Question questionDomain = questionMapper.toSimpleDomain(q);
                    questionDomain.setUpvotes(repository.getQuestionTotalVotesById(q.getId()));

                    qs.add(questionDomain);
                }
        );

        return new GetCourseQuestionsResponse(qs);
    }

    @Override
    public GetCourseQuestionsResponse getByCourseMostUpVoted(Long courseId, int start, int end) {
        var questions = repository.findTopQuestionsByUpVotesAndCourseId(courseId, start, end);

        List<Question> qs = new ArrayList<>();

        questions.forEach(
                q -> {

                    Question questionDomain = questionMapper.toSimpleDomain(q);
                    questionDomain.setUpvotes(repository.getQuestionTotalVotesById(q.getId()));

                    qs.add(questionDomain);

                }
        );

        return new GetCourseQuestionsResponse(qs);
    }
}
