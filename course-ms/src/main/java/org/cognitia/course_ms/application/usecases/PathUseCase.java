package org.cognitia.course_ms.application.usecases;

import lombok.extern.slf4j.Slf4j;
import org.cognitia.course_ms.application.gateways.PathGateway;
import org.cognitia.course_ms.application.gateways.VideoGateway;
import org.cognitia.course_ms.domain.path.Path;
import org.cognitia.course_ms.domain.path.dto.*;
import org.cognitia.course_ms.domain.path.exceptions.InvalidCreatePathRequest;
import org.cognitia.course_ms.domain.path.exceptions.InvalidDeleteVideoFromPathRequest;
import org.cognitia.course_ms.domain.path.exceptions.PathNotFoundException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class PathUseCase {

    private static final String TOPIC = "video.path.delete";
    private final PathGateway pathGateway;
    private final VideoGateway videoGateway;
    private final KafkaTemplate<String, DeleteVideoPathsRequest> deleteVideoTemplate;

    public PathUseCase(PathGateway pathGateway, VideoGateway videoGateway, KafkaTemplate<String, DeleteVideoPathsRequest> deleteVideoTemplate) {
        this.pathGateway = pathGateway;
        this.videoGateway = videoGateway;
        this.deleteVideoTemplate = deleteVideoTemplate;
    }

    public void createPath(CreatePathRequest createPathRequest) {
        if(createPathRequest.courseId() == null || createPathRequest.title() == null||
            createPathRequest.title().isEmpty() || createPathRequest.userId() == null ||
            createPathRequest.userId().isEmpty()) {

            throw new InvalidCreatePathRequest("Please provide a valid data.");
        }

        // validar aqui depois se o usuario que esta criando o path é o dono do curso

        var path = Path.builder()
                .title(createPathRequest.title())
                .courseId(createPathRequest.courseId())
                .build();

        pathGateway.createPath(path);
    }

    public GetPathDataByCourseResponse getPathData(GetPathDataByCourseRequest request){
        if(request.pathId() == null){
            throw new InvalidDeleteVideoFromPathRequest("Please provide a valid data.");
        }

        var path = pathGateway.findById(request.pathId());

        if(path == null){
            throw new PathNotFoundException("Could not find path with id: " + request.pathId());
        }

        var videoData = videoGateway.getPathVideos(request.pathId());

        AtomicReference<Double> totalDuration = new AtomicReference<>(0.0);
        AtomicReference<Long> totalVideos = new AtomicReference<>(0L);

        videoData.videos().forEach(v -> {
            totalDuration.updateAndGet(v1 -> v1 + v.duration());
            totalVideos.updateAndGet(v1 -> v1 + v.path());
        });

        return new GetPathDataByCourseResponse(
                path.getTitle(),
                videoData.videos(),
                totalVideos.get(),
                totalDuration.get()
        );
    }

    public void deletePath(Long pathId){

        // validar aqui se o usuario pode excluir o path;

        log.info("Removendo o path... {}", pathId);

        pathGateway.delete(pathId);
        deleteVideoTemplate.send(
                TOPIC,
                new DeleteVideoPathsRequest(pathId)
        );
        log.info("Path removido com sucesso");
    }

}
