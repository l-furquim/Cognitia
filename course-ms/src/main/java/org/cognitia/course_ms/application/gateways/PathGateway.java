package org.cognitia.course_ms.application.gateways;

import org.cognitia.course_ms.domain.path.Path;
import org.cognitia.course_ms.domain.path.dto.*;

import java.util.List;


public interface PathGateway {

    void createPath(Path pathRequest);
//    List<String> getPathDataByCourse(GetPathDataByCourseRequest request);
    Path findById(Long id);
    void delete(Long id);

}
