package org.cognitia.encoder.application.gateways;

import org.cognitia.encoder.infra.dto.video.VideoProcessingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface S3Gateway {

    void uploadVideoToBucket(MultipartFile video, Long courseId,String lessonName);
    String getUrlByPrefix(String prefix);
    void uploadVideoProcessingResult(VideoProcessingResult result, Long courseId, String videoName);
    File downloadS3RawVideoToTempFile(String lessonName, String videoName, Long courseId, File tempFile);

}
