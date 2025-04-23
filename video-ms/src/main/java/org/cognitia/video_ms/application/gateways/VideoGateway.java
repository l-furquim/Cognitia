package org.cognitia.video_ms.application.gateways;

import org.cognitia.video_ms.domain.entity.Video;
import org.cognitia.video_ms.infra.dto.video.DeleteVideoRequestDto;
import org.cognitia.video_ms.infra.dto.video.GetVideoRequestDto;

public interface VideoGateway {

    void upload(Video video);
    Video findById(Long id);
    void uploadThumb(Long videoId, String thumbUrl);
    void delete(DeleteVideoRequestDto deleteVideoRequestDto);
    String get(GetVideoRequestDto getVideoRequestDto);

}
