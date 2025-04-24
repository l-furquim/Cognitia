package org.cognitia.course_ms.application.usecases;

import lombok.extern.slf4j.Slf4j;
import org.cognitia.course_ms.application.gateways.QuestionGateway;
import org.cognitia.course_ms.domain.question.Question;
import org.cognitia.course_ms.domain.question.dto.CreateQuestionRequest;
import org.cognitia.course_ms.domain.question.exceptions.InvalidQuestionCreateRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;


@Slf4j
@Service
public class QuestionUseCase {

    private final QuestionGateway gateway;
    // mapear gateway do curso aqui

    public QuestionUseCase(QuestionGateway gateway) {
        this.gateway = gateway;
    }

    public void create(CreateQuestionRequest request){
        if
        (
                request.content() == null || request.content().isBlank() ||
                request.authorId() == null || request.authorId().isBlank() ||
                request.authorName() == null || request.authorName().isBlank()
        ){
            throw new InvalidQuestionCreateRequest("Invalid data in request");
        }

        // mapear validacao do curso aqui

        var question = new Question(
                request.content(),
                request.courseId(),
                request.videoId(),
                request.path(),
                LocalDateTime.now(),
                request.authorId(),
                request.authorProfileUrl(),
                request.authorName(),
                new ArrayList<>(),
                new ArrayList<>(),
                null
        );


    }

}
