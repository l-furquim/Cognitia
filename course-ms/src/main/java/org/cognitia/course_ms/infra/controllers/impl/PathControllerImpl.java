package org.cognitia.course_ms.infra.controllers.impl;

import org.cognitia.course_ms.application.usecases.PathUseCase;
import org.cognitia.course_ms.domain.path.dto.CreatePathRequest;
import org.cognitia.course_ms.domain.path.dto.DeleteVideoFromPathRequest;
import org.cognitia.course_ms.domain.path.dto.GetPathDataByCourseRequest;
import org.cognitia.course_ms.domain.path.dto.GetPathDataByCourseResponse;
import org.cognitia.course_ms.infra.controllers.PathController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/api/paths")
public class PathControllerImpl implements PathController {

    private final PathUseCase pathUseCase;

    public PathControllerImpl(PathUseCase pathUseCase) {
        this.pathUseCase = pathUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreatePathRequest request){
        pathUseCase.createPath(request);

        return ResponseEntity.status(201).build();
    }

    @GetMapping("/path/{pathId}")
    public ResponseEntity<GetPathDataByCourseResponse> getByPathId(@PathVariable("pathId") Long pathId) {
        var data = pathUseCase.getPathData(new GetPathDataByCourseRequest(pathId));

        return ResponseEntity.ok().body(data);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody DeleteVideoFromPathRequest request){
        pathUseCase.deleteVideoFromPath(request);

        return ResponseEntity.noContent().build();
    }
}
