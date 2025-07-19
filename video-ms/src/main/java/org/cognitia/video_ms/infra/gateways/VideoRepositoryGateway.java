package org.cognitia.video_ms.infra.gateways;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.cognitia.video_ms.application.gateways.VideoGateway;
import org.cognitia.video_ms.domain.model.Video;
import org.cognitia.video_ms.infra.dto.video.DeleteVideoRequestDto;
import org.cognitia.video_ms.infra.dto.video.GetVideoRequestDto;
import org.cognitia.video_ms.infra.dto.video.UpdateVideoMetadataRequest;
import org.cognitia.video_ms.infra.mappers.VideoMapper;
import org.cognitia.video_ms.infra.persistence.repository.VideoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class VideoRepositoryGateway implements VideoGateway {

    private final VideoMapper videoMapper;
    private final VideoJpaRepository videoJpaRepository;

    public VideoRepositoryGateway(VideoMapper videoMapper, VideoJpaRepository videoJpaRepository) {
        this.videoMapper = videoMapper;
        this.videoJpaRepository = videoJpaRepository;
    }

    @Override
    public Video upload(Video video) {
        var videoEntity = videoMapper.toEntity(video);

        return videoMapper.toDomain(videoJpaRepository.save(videoEntity));
    }

    @Override
    public Video findById(Long id) {
        var videoEntity = videoJpaRepository.findById(id);

        if(videoEntity.isEmpty()) return null;

        return videoMapper.toDomain(videoEntity.get());
    }

    @Transactional
    @Override
    public void uploadThumb(Long videoId, String thumbUrl) {
        var videoEntity = videoJpaRepository.findById(videoId);

        videoEntity.get().setThumbUrl(thumbUrl);

        videoJpaRepository.save(videoEntity.get());
    }

    @Override
    public void delete(DeleteVideoRequestDto deleteVideoRequestDto) {
        var video = videoJpaRepository.findById(deleteVideoRequestDto.videoId());

        if(video.isEmpty()) return;

        videoJpaRepository.delete(video.get());
    }

    @Override
    public String get(GetVideoRequestDto getVideoRequestDto) {
        return "";
    }

    @Override
    public List<Video> getByCourseId(Long courseId) {
        var videos = videoJpaRepository.getByCourseId(courseId);

        return videos.stream().map(
                videoMapper::toDomain
        ).toList();
    }

    @Transactional
    @Override
    public Video update(UpdateVideoMetadataRequest request) {
        var videoEntity = videoJpaRepository.findById(request.videoId());

        if(!videoEntity.isEmpty()){
            if(request.title() != null){
                videoEntity.get().setTitle(request.title());
            }

            if(request.description() != null){
                videoEntity.get().setDescription(request.description());
            }

            if(request.path() != null){
                videoEntity.get().setPath(request.path());
            }

            if(request.skill() != null){
                videoEntity.get().setSkill(request.skill());
            }


            videoJpaRepository.save(videoEntity.get());
        }
        return videoMapper.toDomain(videoEntity.get());
    }

    @Override
    public List<Video> getByPath(Long path) {
        var videosEntity = videoJpaRepository.getByPath(path);

        return videosEntity.stream().map(
                v -> videoMapper.toDomain(
                        v
                )
        ).toList();
    }

    @Override
    public void deleteVideoByPath(Long path) {
        var video = videoJpaRepository.getByPath(path);

        video.forEach(
                v -> {
                    log.info("Deleting video with path: " + path);
                    videoJpaRepository.delete(v);
                }
        );
    }
}
