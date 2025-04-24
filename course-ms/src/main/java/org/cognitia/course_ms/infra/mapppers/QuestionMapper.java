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
                .content(question.content())
                .courseId(question.courseId())
                .questionedAt(question.questionedAt())
                .path(question.path())
                .authorProfileUrl(question.authorProfileUrl())
                .authorId(question.authorId())
                .authorName(question.authorName())
                .videoId(question.videoId())
                .answers(new ArrayList<>())
                .build();
    }

    public Question toDomain(QuestionEntity question){
        return new Question(
                question.getContent(),
                question.getCourseId(),
                question.getVideoId(),
                question.getPath(),
                question.getQuestionedAt(),
                question.getAuthorId(),
                question.getAuthorProfileUrl(),
                question.getAuthorName(),
                question.getAnswers().stream().map( an -> this.toDomain(an) ).toList(),
                this.toDomain(question.getParent())
        );
    }

}
