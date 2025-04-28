package org.cognitia.course_ms.infra.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double rate;

    private String description;

    private LocalDateTime reviwedAt;

    private String authorId;

    private String authorName;

    private String authorProfileUrl;

    private Long courseId;
}
