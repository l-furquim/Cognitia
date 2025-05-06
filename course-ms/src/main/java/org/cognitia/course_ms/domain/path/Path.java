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

    private Long courseId;
}
