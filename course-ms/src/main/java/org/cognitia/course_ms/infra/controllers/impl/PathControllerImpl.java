package org.cognitia.course_ms.infra.controllers.impl;

import org.cognitia.course_ms.application.usecases.PathUseCase;
import org.cognitia.course_ms.domain.path.dto.CreatePathRequest;
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

    @DeleteMapping("/path/{pathId}")
    public ResponseEntity<Void> delete(@PathVariable("pathId") Long pathId) {
        pathUseCase.deletePath(pathId);

        return ResponseEntity.status(204).build();
    }
}
