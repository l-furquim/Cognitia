package org.cognitia.course_ms.infra.controllers.impl;

import org.cognitia.course_ms.application.usecases.UpVoteUseCase;
import org.cognitia.course_ms.domain.UpVote.dto.CreateUpVoteRequest;
import org.cognitia.course_ms.domain.UpVote.dto.DeleteUpVoteRequest;
import org.cognitia.course_ms.domain.UpVote.dto.GetUpVotesByAuthorResponse;
import org.cognitia.course_ms.infra.controllers.UpVoteController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/api/upvotes")
public class UpVoteControllerImpl implements UpVoteController {

    private final UpVoteUseCase upVoteUseCase;

    public UpVoteControllerImpl(UpVoteUseCase upVoteUseCase) {
        this.upVoteUseCase = upVoteUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateUpVoteRequest request){
        upVoteUseCase.create(request);

        return ResponseEntity.status(201).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody DeleteUpVoteRequest request){
        upVoteUseCase.unDoUpvote(request);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("{authorId}")
    public ResponseEntity<GetUpVotesByAuthorResponse> getByAuthor(@PathVariable("authorId") String authorId){
        var upvotes = upVoteUseCase.getByAuthor(authorId);

        return ResponseEntity.ok().body(new GetUpVotesByAuthorResponse(upvotes));
    }

}
