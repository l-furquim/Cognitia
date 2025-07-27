package org.cognitia.encoder.infra.dto.video;

import org.cognitia.encoder.domain.model.Video;

import java.util.List;

public record GetPathVideosResponse(
        List<Video> videos
) {
}
