package org.cognitia.video_ms.infra.dto.video;

import org.cognitia.video_ms.domain.entity.Video;

import java.util.List;

public record GetCourseVideosResponse(
        List<Video> videos
) {
}
