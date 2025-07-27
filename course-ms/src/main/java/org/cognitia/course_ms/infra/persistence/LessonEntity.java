package org.cognitia.course_ms.infra.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class LessonEntity {

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
