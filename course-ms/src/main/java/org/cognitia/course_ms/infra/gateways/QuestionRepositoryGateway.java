package org.cognitia.course_ms.infra.gateways;

import jakarta.transaction.Transactional;
import org.cognitia.course_ms.application.gateways.QuestionGateway;
import org.cognitia.course_ms.domain.question.Question;
import org.cognitia.course_ms.infra.mapppers.QuestionMapper;
import org.cognitia.course_ms.infra.persistence.repository.QuestionJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestionRepositoryGateway implements QuestionGateway {

    private final QuestionJpaRepository repository;
    private final QuestionMapper questionMapper;

    public QuestionRepositoryGateway(QuestionJpaRepository repository, QuestionMapper questionMapper) {
        this.repository = repository;
        this.questionMapper = questionMapper;
    }

    @Transactional
    @Override
    public void addUpVote(Long upVoteId, Long questionId) {
        var question = repository.findById(questionId);

        if(question.isPresent()) question.get().addUpVote(upVoteId); repository.save(question.get());
    }

    @Transactional
    @Override
    public void removeUpVote(Long upVoteId, Long questionId) {
        var question = repository.findById(questionId);

        if(question.isPresent()) question.get().removeUpVote(upVoteId); repository.save(question.get());
    }

    @Override
    public void create(Question question) {
        var questionEntity = questionMapper.toEntity(question);

        repository.save(questionEntity);
    }

    @Override
    public void delete(Long id) {
        var question = repository.findById(id);

        if(question.isPresent()) repository.delete(question.get());
    }

    @Override
    public List<Question> getByVideoId(Long videoId) {
        return repository.getByVideoId(videoId).stream().map(
                q -> questionMapper.toDomain(q)
        ).toList();
    }

    @Override
    public Integer getCourseTotalQuestions(Long courseId) {
        return repository.getCourseTotalQuestions(courseId);
    }

    @Override
    public List<Question> getByAuthorId(String authorId) {
        return repository.getByAuthorId(authorId).stream().map(
                q -> questionMapper.toDomain(q)
        ).toList();
    }
}
