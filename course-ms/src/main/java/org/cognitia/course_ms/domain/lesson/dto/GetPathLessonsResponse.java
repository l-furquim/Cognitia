package org.cognitia.course_ms.domain.lesson.dto;

import org.cognitia.video_ms.domain.model.Video;

import java.util.List;

public record GetPathLessonsResponse(
        List<Video> videos
) {
}
