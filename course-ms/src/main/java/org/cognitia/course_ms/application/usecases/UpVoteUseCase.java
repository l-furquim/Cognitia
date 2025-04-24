package org.cognitia.course_ms.application.usecases;

import lombok.extern.slf4j.Slf4j;
import org.cognitia.course_ms.application.gateways.QuestionGateway;
import org.cognitia.course_ms.application.gateways.UpVoteGateway;
import org.cognitia.course_ms.domain.UpVote.exceptions.*;
import org.cognitia.course_ms.domain.UpVote.dto.CreateUpVoteRequest;
import org.cognitia.course_ms.domain.UpVote.dto.DeleteUpVoteRequest;
import org.cognitia.course_ms.domain.UpVote.UpVote;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class UpVoteUseCase {

    private final UpVoteGateway gateway;
    private final QuestionGateway questionGateway;

    public UpVoteUseCase
            (
            UpVoteGateway gateway,
            QuestionGateway questionGateway,
            UpVoteGateway upVoteGateway
    ) {
        this.gateway = gateway;
        this.questionGateway = questionGateway;
    }

    public void create(CreateUpVoteRequest  request){
        if(request.authorId() == null || request.authorId().isEmpty()){
            throw new InvalidUpVoteRequest("Author id cannot be null or empty");
        }

        // Chamar o open feign aqui depois para validar o usuario.

        var questionExists = questionGateway.findById(request.questionId());

        if(questionExists == null){
            throw new UpvoteQuestionNotFound("Could not found the question for the upvote");
        }

        var userAlreadyUpVotedThatQuestion = gateway.findByQuestionAndAuthorId(request.authorId(), request.questionId());

        if(userAlreadyUpVotedThatQuestion != null){
            throw new UpVoteAlreadyDoneException("Cannot upvote two times at the same question");
        }

        var upVote = new UpVote(
                LocalDateTime.now(),
                request.authorId(),
                request.questionId()
        );

        gateway.create(upVote);
    }

    public void unDoUpvote(DeleteUpVoteRequest request){
        if(request.upVoteId() == null){
            throw new InvalidDeleteUpvoteRequest("Upvote id is null, so cannot be undone");
        }
        gateway.delete(request.upVoteId());
    }

    public List<UpVote> getByAuthor(String authorId){
        if(authorId == null){
            throw new InvalidGetUpvotesByAuthorRequest("Author id is null, so cannot find the upvotes");
        }

        return gateway.getByAuthor(authorId);
    }


}
