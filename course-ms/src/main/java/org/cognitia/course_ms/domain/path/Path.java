package org.cognitia.course_ms.domain.path;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Path {
    private String title;

    private List<Long> videosId;

    private Long courseId;

    public void addVideoId(Long videoId) {
        this.videosId.add(videoId);
    }

    public void removeVideoId(Long videoId) {
        this.videosId.remove(videoId);
    }

}
