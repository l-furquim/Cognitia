package org.cognitia.course_ms.infra.controllers.impl;

import org.cognitia.course_ms.application.usecases.QuestionUseCase;
import org.cognitia.course_ms.domain.UpVote.dto.DeleteUpVoteRequest;
import org.cognitia.course_ms.domain.question.dto.CreateQuestionRequest;
import org.cognitia.course_ms.domain.question.dto.GetCourseQuestionsRequest;
import org.cognitia.course_ms.domain.question.dto.GetCourseQuestionsResponse;
import org.cognitia.course_ms.domain.question.dto.GetVideoQuestionsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/api/questions")
public class QuestionControllerImpl {

    private final QuestionUseCase useCase;

    public QuestionControllerImpl(QuestionUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateQuestionRequest request){
        useCase.create(request);

        return ResponseEntity.status(201).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUpVote(@RequestBody DeleteUpVoteRequest request){
        useCase.removeUpVote(request);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<GetVideoQuestionsResponse> getByVideo(@PathVariable("videoId") Long videoId){
        var videos = useCase.getByVideoId(videoId);

        return ResponseEntity.ok().body(new GetVideoQuestionsResponse(videos));
    }

    @GetMapping("/course")
    public ResponseEntity<GetCourseQuestionsResponse> getByCourse(@RequestBody GetCourseQuestionsRequest request){
        var questions = useCase.getByCourse(request);

        return ResponseEntity.ok().body(questions);
    }


}
