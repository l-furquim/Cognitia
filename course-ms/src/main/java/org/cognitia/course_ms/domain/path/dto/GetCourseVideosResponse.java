package org.cognitia.course_ms.domain.path.dto;

import java.util.List;

public record GetCourseVideosResponse(
        List<VideoDto> videos
) {
}
