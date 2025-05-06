package org.cognitia.course_ms.application.gateways;

import org.cognitia.course_ms.domain.path.dto.GetCourseVideosResponse;

public interface VideoGateway {

    GetCourseVideosResponse getPathVideos(Long pathId);

}
