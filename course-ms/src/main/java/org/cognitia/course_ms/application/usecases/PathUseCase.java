package org.cognitia.course_ms.application.usecases;

import org.cognitia.course_ms.application.gateways.PathGateway;
import org.cognitia.course_ms.application.gateways.VideoGateway;
import org.cognitia.course_ms.domain.path.Path;
import org.cognitia.course_ms.domain.path.dto.*;
import org.cognitia.course_ms.domain.path.exceptions.InvalidAddVideoToPathRequest;
import org.cognitia.course_ms.domain.path.exceptions.InvalidCreatePathRequest;
import org.cognitia.course_ms.domain.path.exceptions.InvalidDeleteVideoFromPathRequest;
import org.cognitia.course_ms.domain.path.exceptions.PathNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class PathUseCase {

    private final PathGateway pathGateway;
    private final VideoGateway videoGateway;

    public PathUseCase(PathGateway pathGateway, VideoGateway videoGateway) {
        this.pathGateway = pathGateway;
        this.videoGateway = videoGateway;
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
                .videosId(new ArrayList<>())
                .build();

        pathGateway.createPath(path);
    }

    public void addVideoToPath(MultipartFile file, AddVideoToPathRequest request){
        if(request.pathId() == null || request.videoId() == null ||
        request.userId() == null || request.userId().isEmpty()) {
            throw new InvalidAddVideoToPathRequest("Please provide a valid data.");
        }

        var path = pathGateway.findById(request.pathId());

        if(path == null){
            throw new PathNotFoundException("Could not find path with id: " + request.pathId());
        }
        pathGateway.addVideoToPath(request);
    }


    public void deleteVideoFromPath(DeleteVideoFromPathRequest request){
        if(request.pathId() == null || request.videoId() == null ||
        request.userId() == null || request.userId().isEmpty()) {
            throw new InvalidDeleteVideoFromPathRequest("Please provide a valid data.");
        }

        // validar aqui depois se o usuario que esta criando o path é o dono do curso

        new Thread(() -> {
            videoGateway.deleteVideoById(request.videoId());
        }).start();


        pathGateway.deleteVideoFromPath(request);
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

}
