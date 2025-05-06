package org.cognitia.course_ms.infra.gateways;

import lombok.extern.slf4j.Slf4j;
import org.cognitia.course_ms.application.gateways.VideoGateway;
import org.cognitia.course_ms.domain.path.dto.GetCourseVideosResponse;
import org.cognitia.course_ms.domain.path.exceptions.VideoClientException;
import org.cognitia.course_ms.infra.client.VideoClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VideoClientGateway implements VideoGateway {

    private final VideoClient videoClient;

    public VideoClientGateway(VideoClient videoClient) {
        this.videoClient = videoClient;
    }

    @Override
    public GetCourseVideosResponse getPathVideos(Long pathId) {
        try{
            var videos = videoClient.getByPath(pathId);

            return videos.getBody();
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
