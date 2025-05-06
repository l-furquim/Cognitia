package org.cognitia.course_ms.domain.path.dto;

import java.util.List;

public record GetPathDataByCourseResponse(
        String title,
        List<PathWithVideoDto> paths,
        Long totalVideos,
        Long totalDuration
) {
}
