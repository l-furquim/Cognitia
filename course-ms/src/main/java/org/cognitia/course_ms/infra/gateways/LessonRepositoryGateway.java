package org.cognitia.course_ms.infra.gateways;

import lombok.extern.slf4j.Slf4j;
import org.cognitia.course_ms.application.gateways.LessonGateway;
import org.cognitia.course_ms.domain.path.dto.GetCourseVideosResponse;
import org.cognitia.course_ms.domain.path.dto.VideoDto;
import org.cognitia.course_ms.domain.path.exceptions.VideoClientException;
import org.cognitia.course_ms.infra.client.VideoClient;
import org.cognitia.course_ms.infra.mapppers.LessonMapper;
import org.cognitia.course_ms.infra.persistence.repository.LessonJpaRepository;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LessonRepositoryGateway implements LessonGateway {

    private final LessonJpaRepository repository;
    private final LessonMapper mapper;

    public LessonRepositoryGateway(LessonJpaRepository repository, LessonMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public GetCourseVideosResponse getPathLessons(Long pathId) {
        try{
            var videos = repository.getByPath(pathId);

            return new GetCourseVideosResponse(
                    videos
                            .stream()
                            .map(l -> new VideoDto(
                                    l.getTitle(),
                                    l.getDescription(),
                                    l.getOriginalName(),
                                    l.getPath(),
                                    l.getSkill(),
                                    l.getDuration(),
                                    l.getChunksUrl(),
                                    l.getThumbUrl(),
                                    l.getCourseId()
                            ))
                            .toList()
            );
        }catch (Exception e){
            log.error("Erro ao buscar os videos no client {}", e.getMessage());
            throw new VideoClientException(e.getMessage());
        }
    }

    @Override
    public void deleteVideoById(Long videoId) {
        try{
            videoClient.deleteByVideo(videoId);
        }catch (Exception e){
            log.error("Erro remover o video do client {}", e.getMessage());
            throw new VideoClientException(e.getMessage());
        }
    }


}
