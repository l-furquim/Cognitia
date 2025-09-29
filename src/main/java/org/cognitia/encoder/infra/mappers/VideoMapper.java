package org.cognitia.encoder.infra.mappers;

import org.cognitia.encoder.domain.model.Video;
import org.cognitia.encoder.infra.persistence.VideoEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class VideoMapper {

    public VideoEntity toEntity(Video video) {
        VideoEntity entity = new VideoEntity(
                video.name(),
                video.status(),
                video.durationInMinutes(),
                video.extension(),
                video.thumbUrl(),
                video.playlistsUrl(),
                video.playbackUrl(),
                video.originalFileSize(),
                video.failedReason()
        );
        if(video.id() != null) {
            entity.setId(video.id());
        } else {
            entity.setId(UUID.randomUUID().toString());
        }

        return entity;
    }

    public Video toDomain(VideoEntity entity) {
        return new Video(
                entity.getId(),
                entity.getName(),
                entity.getStatus(),
                entity.getDurationInMinutes(),
                entity.getExtension(),
                entity.getThumbUrl(),
                entity.getPlaylistsUrl(),
                entity.getPlaybackUrl(),
                entity.getOriginalFileSize(),
                entity.getFailedReason()
        );
    }
}