package org.cognitia.course_ms.infra.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PathEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
