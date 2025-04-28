package org.cognitia.course_ms.application.usecases;

import lombok.extern.slf4j.Slf4j;
import org.cognitia.course_ms.application.gateways.QuestionGateway;
import org.cognitia.course_ms.domain.UpVote.dto.DeleteUpVoteRequest;
import org.cognitia.course_ms.domain.UpVote.exceptions.InvalidUpVoteDeleteRequest;
import org.cognitia.course_ms.domain.question.Question;
import org.cognitia.course_ms.domain.question.dto.CreateQuestionRequest;
import org.cognitia.course_ms.domain.question.dto.GetCourseQuestionsRequest;
import org.cognitia.course_ms.domain.question.dto.GetCourseQuestionsResponse;
import org.cognitia.course_ms.domain.question.exceptions.InvalidGetCourseQuestionRequest;
import org.cognitia.course_ms.domain.question.exceptions.InvalidGetVideoQuestionsRequest;
import org.cognitia.course_ms.domain.question.exceptions.InvalidQuestionCreateRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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

        var question = Question.builder()
                .content(request.content())
                .courseId(request.courseId())
                .videoId(request.videoId())
                .path(request.path())
                .questionedAt(LocalDateTime.now())
                .authorId(request.authorId())
                .authorProfileUrl(request.authorProfileUrl())
                .authorName(request.authorName())
                .answers(new ArrayList<>())
                .parent(null)
                .upvotes(0L)
                .build();

        var newQuestionId = gateway.create(question);

        if(request.parentId() != null){
            gateway.addQuestionToBranch(newQuestionId, request.parentId());
        }

        // aletar gateway de curso que um novo comentario foi adicionado
    }

    public void removeUpVote(DeleteUpVoteRequest request){
        if(request.upVoteId() == null){
            throw new InvalidUpVoteDeleteRequest("Cannot remove a upvote with a null id");
        }


        // validar aqui se o usuario que esta removendo o upvote foi quem criou o mesmo

        gateway.delete(request.upVoteId());
    }

    public List<Question> getByVideoId(Long videoId){
        if(videoId == null){
            throw new InvalidGetVideoQuestionsRequest("Cannot find the questions for a video id null");
        }

        var questions = gateway.getByVideoId(videoId);

        // validar aqui se o usuario logado esta cadastrado no curso

        return questions;
    }

    public GetCourseQuestionsResponse getByCourse(GetCourseQuestionsRequest request){
        if(request.courseId() == null){
            throw new InvalidGetCourseQuestionRequest("Invalid id for the course videos search");
        }

        GetCourseQuestionsResponse questionsToBeReturned = null;

        if(request.shortOption().recent()){
            if(request.videoId() != null){
                questionsToBeReturned = gateway.getByVideoMostRecents(request.videoId(), request.pageStart(), request.pageEnd());
            }else{
                questionsToBeReturned = gateway.getByCourseMostRecents(request.courseId(), request.pageStart(), request.pageEnd());
            }
        }

        if(request.shortOption().mostUpVoted()){
            if(request.videoId() != null){
                questionsToBeReturned = gateway.getByVideoMostUpVoted(request.videoId(), request.pageEnd(), request.pageStart());
            }else{
                questionsToBeReturned = gateway.getByCourseMostUpVoted(request.courseId(), request.pageEnd(), request.pageStart());
            }
        }
        return new GetCourseQuestionsResponse(questionsToBeReturned.questions());
    }

}