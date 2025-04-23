package org.cognitia.video_ms.application.gateways;

import org.cognitia.video_ms.domain.model.Video;
import org.cognitia.video_ms.infra.dto.video.DeleteVideoRequestDto;
import org.cognitia.video_ms.infra.dto.video.GetVideoRequestDto;
import org.cognitia.video_ms.infra.dto.video.UpdateVideoMetadataRequest;

import java.util.List;

public interface VideoGateway {

    void upload(Video video);
    Video findById(Long id);
    void uploadThumb(Long videoId, String thumbUrl);
    void delete(DeleteVideoRequestDto deleteVideoRequestDto);
    String get(GetVideoRequestDto getVideoRequestDto);
    List<Video> getByCourseId(Long courseId);
    Video update(UpdateVideoMetadataRequest request);


}
