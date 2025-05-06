package org.cognitia.course_ms.application.usecases;

import org.cognitia.course_ms.application.gateways.PathGateway;
import org.cognitia.course_ms.application.gateways.VideoGateway;
import org.cognitia.course_ms.domain.path.Path;
import org.cognitia.course_ms.domain.path.dto.AddVideoToPathRequest;
import org.cognitia.course_ms.domain.path.dto.CreatePathRequest;
import org.cognitia.course_ms.domain.path.exceptions.InvalidAddVideoToPathRequest;
import org.cognitia.course_ms.domain.path.exceptions.InvalidCreatePathRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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

    public void addVideoToPath(AddVideoToPathRequest request){
        if(request.pathId() == null || request.videoId() == null ||
        request.userId() == null || request.userId().isEmpty()) {
            throw new InvalidAddVideoToPathRequest("Please provide a valid data.");
        }

        
    }

}
