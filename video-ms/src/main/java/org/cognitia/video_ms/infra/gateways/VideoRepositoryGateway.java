package org.cognitia.video_ms.infra.gateways;

import org.cognitia.video_ms.application.gateways.VideoGateway;
import org.cognitia.video_ms.domain.entity.Video;
import org.cognitia.video_ms.infra.dto.video.DeleteVideoRequestDto;
import org.cognitia.video_ms.infra.dto.video.GetVideoRequestDto;
import org.cognitia.video_ms.infra.dto.video.UploadVideoResponse;
import org.cognitia.video_ms.infra.mappers.VideoMapper;
import org.cognitia.video_ms.infra.persistence.repository.VideoJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class VideoRepositoryGateway implements VideoGateway {

    private final VideoMapper videoMapper;
    private final VideoJpaRepository videoJpaRepository;

    public VideoRepositoryGateway(VideoMapper videoMapper, VideoJpaRepository videoJpaRepository) {
        this.videoMapper = videoMapper;
        this.videoJpaRepository = videoJpaRepository;
    }

    @Override
    public void upload(Video video) {
        var videoEntity = videoMapper.toEntity(video);

        videoJpaRepository.save(videoEntity);
    }

    @Override
    public void delete(DeleteVideoRequestDto deleteVideoRequestDto) {

    }

    @Override
    public String get(GetVideoRequestDto getVideoRequestDto) {
        return "";
    }
}
