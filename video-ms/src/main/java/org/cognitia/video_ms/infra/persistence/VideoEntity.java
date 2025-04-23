package org.cognitia.video_ms.infra.persistence;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class VideoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String originalName;

    private Long path;

    private String skill;

    private Double duration;

    private String chunksUrl;

    private String thumbUrl;

    @Column(name = "course_id")
    private Long courseId;

}
