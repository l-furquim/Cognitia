package org.cognitia.course_ms.infra.mapppers;

import org.cognitia.course_ms.domain.question.Question;
import org.cognitia.course_ms.infra.persistence.QuestionEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class QuestionMapper {

    private final UpVoteMapper upVoteMapper;

    public QuestionMapper(UpVoteMapper upVoteMapper) {
        this.upVoteMapper = upVoteMapper;
    }

    public QuestionEntity toEntity(Question question){
        return QuestionEntity.builder()
                .content(question.getContent())
                .courseId(question.getCourseId())
                .questionedAt(question.getQuestionedAt())
                .path(question.getPath())
                .authorProfileUrl(question.getAuthorProfileUrl())
                .authorId(question.getAuthorId())
                .authorName(question.getAuthorName())
                .videoId(question.getVideoId())
                .answers(new ArrayList<>())
                .build();
    }

    public Question toDomain(QuestionEntity question, boolean mapParent) {
        return Question.builder()
                .content(question.getContent())
                .courseId(question.getCourseId())
                .videoId(question.getVideoId())
                .path(question.getPath())
                .questionedAt(question.getQuestionedAt())
                .authorId(question.getAuthorId())
                .authorProfileUrl(question.getAuthorProfileUrl())
                .authorName(question.getAuthorName())
                .answers(
                        question.getAnswers().stream()
                                .map(an -> this.toDomain(an, false)) // Não realiza a busca para que não ocorra o stack overflow
                                .toList()
                )
                .parent(
                        mapParent && question.getParent() != null ? this.toDomain(question.getParent(), false) : null
                )
                .build();
    }

    public Question toSimpleDomain(QuestionEntity question){
        return Question.builder()
                .content(question.getContent())
                .courseId(question.getCourseId())
                .videoId(question.getVideoId())
                .path(question.getPath())
                .questionedAt(question.getQuestionedAt())
                .authorId(question.getAuthorId())
                .authorProfileUrl(question.getAuthorProfileUrl())
                .authorName(question.getAuthorName())
                .answers(new ArrayList<>())
                .parent(null)
                .build();
    }
}
