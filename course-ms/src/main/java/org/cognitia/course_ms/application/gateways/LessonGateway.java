package org.cognitia.course_ms.application.gateways;

import org.cognitia.course_ms.domain.path.dto.GetCourseVideosResponse;

public interface LessonGateway {

    GetCourseVideosResponse getPathLessons(Long pathId);
    void deleteLessonById(Long videoId);

}
