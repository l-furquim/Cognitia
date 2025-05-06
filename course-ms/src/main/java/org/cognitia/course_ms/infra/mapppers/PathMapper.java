package org.cognitia.course_ms.infra.mapppers;

import org.cognitia.course_ms.domain.path.Path;
import org.cognitia.course_ms.infra.persistence.PathEntity;
import org.springframework.stereotype.Component;

@Component
public class PathMapper {

    public Path toDomain(PathEntity pathEntity) {
        return Path.builder()
                .title(pathEntity.getTitle())
                .courseId(pathEntity.getCourseId())
                .build();
    }

    public PathEntity toEntity(Path path) {
        return PathEntity.builder()
                .title(path.getTitle())
                .courseId(path.getCourseId())
                .build();
    }

}
