package org.cognitia.video_ms.application.gateways;

import org.cognitia.video_ms.domain.entity.Video;
import org.cognitia.video_ms.infra.dto.video.DeleteVideoRequestDto;
import org.cognitia.video_ms.infra.dto.video.GetVideoRequestDto;
import org.cognitia.video_ms.infra.dto.video.UploadVideoResponse;

public interface VideoGateway {

    void upload(Video video);
    void delete(DeleteVideoRequestDto deleteVideoRequestDto);
    String get(GetVideoRequestDto getVideoRequestDto);

}
