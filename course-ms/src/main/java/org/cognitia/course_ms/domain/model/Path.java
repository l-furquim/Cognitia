package org.cognitia.course_ms.domain.model;

import java.util.List;

public record Path(
        String title,
        List<Long> videosId,
        Boolean allowedToWatch
) {
}
