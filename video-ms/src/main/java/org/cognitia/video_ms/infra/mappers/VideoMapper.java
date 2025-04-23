package org.cognitia.video_ms.infra.mappers;

import org.cognitia.video_ms.domain.entity.Video;
import org.cognitia.video_ms.infra.persistence.VideoEntity;
import org.springframework.stereotype.Component;


@Component
public class VideoMapper {

    public VideoEntity toEntity(Video video){
        return VideoEntity.builder()
                .title(video.title())
                .description(video.description())
                .skill(video.skill())
                .path(video.path())
                .duration(video.duration())
                .courseId(video.courseId())
                .thumbUrl(video.thumbUrl())
                .build();
    }

    public Video toDomain(VideoEntity videoEntity){
        return new Video(
                videoEntity.getTitle(),
                videoEntity.getDescription(),
                videoEntity.getPath(),
                videoEntity.getSkill(),
                videoEntity.getDuration(),
                videoEntity.getThumbUrl(),
                videoEntity.getCourseId()
        );
    }

}
