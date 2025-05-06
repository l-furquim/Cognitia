package org.cognitia.course_ms.application.gateways;

import org.cognitia.course_ms.domain.path.Path;
import org.cognitia.course_ms.domain.path.dto.*;

import java.util.List;


public interface PathGateway {

    void createPath(Path pathRequest);
    void addVideoToPath(AddVideoToPathRequest request);
    void deleteVideoFromPath(DeleteVideoFromPathRequest request);
//    List<String> getPathDataByCourse(GetPathDataByCourseRequest request);
    Path findById(Long id);


}
