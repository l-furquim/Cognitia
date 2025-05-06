package org.cognitia.course_ms.infra.gateways;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.cognitia.course_ms.application.gateways.PathGateway;
import org.cognitia.course_ms.domain.path.Path;
import org.cognitia.course_ms.domain.path.dto.*;
import org.cognitia.course_ms.infra.mapppers.PathMapper;
import org.cognitia.course_ms.infra.persistence.repository.PathJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
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


//    @Override
//    public List<String> getPathDataByCourse(GetPathDataByCourseRequest request) {
//        var pathsEntity = pathJpaRepository.findByCourseId(request.courseId());
//
//        return pathsEntity.stream().map(
//                p -> p.getTitle()
//        ).toList();
//    }

    @Override
    public Path findById(Long id) {
        var path = pathJpaRepository.findById(id);

        if(path.isPresent()){
            return pathMapper.toDomain(path.get());
        }

        return null;
    }

    @Override
    public void delete(Long id) {
        var path = pathJpaRepository.findById(id);

        if(path.isEmpty()){
            log.warn("Path with id {} not found", id);
            return;
        }
        pathJpaRepository.delete(path.get());
    }
}
