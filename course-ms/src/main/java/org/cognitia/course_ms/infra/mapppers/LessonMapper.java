package org.cognitia.course_ms.infra.mapppers;

import org.cognitia.course_ms.infra.persistence.LessonEntity;
import org.cognitia.video_ms.domain.model.Video;
import org.cognitia.video_ms.infra.persistence.VideoEntity;
import org.springframework.stereotype.Component;


@Component
public class LessonMapper {

    public LessonEntity toEntity(Lesson video){
        return LessonEntity.builder()
                .title(video.title())
                .description(video.description())
                .originalName(video.originalName())
                .skill(video.skill())
                .path(video.path())
                .duration(video.duration())
                .courseId(video.courseId())
                .chunksUrl(video.chunksUrl())
                .thumbUrl(video.thumbUrl())
                .build();
    }

    public Lesson toDomain(LessonEntity videoEntity){
        return new Video(
                videoEntity.getTitle(),
                videoEntity.getDescription(),
                videoEntity.getOriginalName(),
                videoEntity.getPath(),
                videoEntity.getSkill(),
                videoEntity.getDuration(),
                videoEntity.getChunksUrl(),
                videoEntity.getThumbUrl(),
                videoEntity.getCourseId()
        );
    }

}
