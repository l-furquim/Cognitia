package org.cognitia.course_ms.application.gateways;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

public interface S3Gateway {

    void uploadVideoToBucket(Path video, Long courseId, String videoName);
    void uploadThumbToBucket(MultipartFile thumb, Long courseId, String videoName);
    List<String> getVideoUrlsFromCourse(Long courseId);
    String getUrlByPrefix(String prefix);
    void deleteObjectFromBucket(String objectKey);
    void uploadVideoProcessingResult(VideoProcessingResult result, Long courseId, String videoName);
    VideoS3Structure getVideoStructure(Long courseId, String videoName);
    void deleteVideoDirectory(Long courseId, String videoName);

}
