package org.cognitia.video_ms.application.gateways;


import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

public interface S3Gateway {

    void uploadVideoToBucket(Path video, Long courseId, String videoName);
    List<String> getVideoUrlsFromCourse(Long courseId);
    public void deleteObjectFromBucket(String objectKey);
    String getUrlByPrefix(String prefix);
    void uploadThumbToBucket(MultipartFile thumb, Long courseId, String videoName);

}
