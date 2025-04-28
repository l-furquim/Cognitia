package org.cognitia.course_ms.application.usecases;

import lombok.extern.slf4j.Slf4j;
import org.cognitia.course_ms.application.gateways.QuestionGateway;
import org.cognitia.course_ms.application.gateways.ReviewGateway;
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
    private final ReviewGateway reviewGateway;

    public UpVoteUseCase
            (
            UpVoteGateway gateway,
            QuestionGateway questionGateway,
            UpVoteGateway upVoteGateway,
            ReviewGateway reviewGateway
    ) {
        this.gateway = gateway;
        this.questionGateway = questionGateway;
        this.reviewGateway = reviewGateway;
    }

    public void create(CreateUpVoteRequest  request){
        if(request.authorId() == null || request.authorId().isEmpty()
        || (request.reviewId() == null && request.questionId() == null)
        ){
            throw new InvalidUpVoteRequest("Invalid data for a upvote");
        }

        validateUpVote(request);

        var upVote = new UpVote(
                LocalDateTime.now(),
                request.authorId(),
                request.questionId(),
                request.reviewId()
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

    private void validateUpVote(CreateUpVoteRequest  request){
        // Chamar o open feign aqui depois para validar o usuario.

        if(request.questionId() != null){
            var questionExists = questionGateway.findById(request.questionId());

            if(questionExists == null){
                throw new UpvoteQuestionNotFound("Could not found the question for the upvote");
            }

            var userAlreadyUpVotedThatQuestion = gateway.findByQuestionAndAuthorId(request.authorId(), request.questionId());

            if(userAlreadyUpVotedThatQuestion != null){
                throw new UpVoteAlreadyDoneException("Cannot upvote two times at the same question");
            }

            return;
        }
    }

}
