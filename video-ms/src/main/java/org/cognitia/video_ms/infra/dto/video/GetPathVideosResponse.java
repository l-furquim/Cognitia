package org.cognitia.video_ms.infra.dto.video;

import org.cognitia.video_ms.domain.model.Video;

import java.util.List;

public record GetPathVideosResponse(
        List<Video> videos
) {
}
