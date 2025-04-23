package org.cognitia.video_ms.application.gateways;


import java.nio.file.Path;
import java.util.List;

public interface S3Gateway {

    void uploadFileToBucket(Path video, Long courseId);
    List<String> getVideoUrlsFromCourse(Long courseId);
    public void deleteObjectFromBucket(String objectKey);
    String getVideoUrl(String prefix);

}
