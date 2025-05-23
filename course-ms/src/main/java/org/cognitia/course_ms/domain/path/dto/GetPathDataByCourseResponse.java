package org.cognitia.course_ms.domain.path.dto;

import java.util.List;

public record GetPathDataByCourseResponse(
        String title,
        List<VideoDto> videos,
        Long totalVideos,
        Double totalDuration
) {
}
