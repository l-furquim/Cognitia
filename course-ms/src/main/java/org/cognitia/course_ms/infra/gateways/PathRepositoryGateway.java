package org.cognitia.course_ms.infra.gateways;

import jakarta.transaction.Transactional;
import org.cognitia.course_ms.application.gateways.PathGateway;
import org.cognitia.course_ms.domain.path.Path;
import org.cognitia.course_ms.domain.path.dto.*;
import org.cognitia.course_ms.infra.mapppers.PathMapper;
import org.cognitia.course_ms.infra.persistence.repository.PathJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PathRepositoryGateway implements PathGateway {

    private final PathJpaRepository pathJpaRepository;
    private final PathMapper pathMapper;

    public PathRepositoryGateway(PathJpaRepository pathJpaRepository, PathMapper pathMapper) {
        this.pathJpaRepository = pathJpaRepository;
        this.pathMapper = pathMapper;
    }

    @Override
    public void createPath(Path pathRequest) {
        var pathEntity = pathMapper.toEntity(pathRequest);

        pathJpaRepository.save(pathEntity);
    }

    @Transactional
    @Override
    public void addVideoToPath(AddVideoToPathRequest request) {
        var pathEntity = pathJpaRepository.findById(request.pathId());

        pathEntity.get().addVideoId(request.videoId());

        pathJpaRepository.save(pathEntity.get());
    }

    @Transactional
    @Override
    public void deleteVideoFromPath(DeleteVideoFromPathRequest request) {
        var pathEntity = pathJpaRepository.findById(request.pathId());

        pathEntity.get().removeVideoId(request.videoId());

        pathJpaRepository.save(pathEntity.get());
    }

    @Override
    public List<String> getPathDataByCourse(GetPathDataByCourseRequest request) {
        var pathsEntity = pathJpaRepository.findByCourseId(request.courseId());

        return pathsEntity.stream().map(
                p -> p.getTitle()
        ).toList();
    }
}
