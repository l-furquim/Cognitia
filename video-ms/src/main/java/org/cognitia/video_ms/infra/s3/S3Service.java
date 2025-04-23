package org.cognitia.video_ms.infra.s3;

import org.cognitia.video_ms.application.gateways.S3Gateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class S3Service implements S3Gateway {

    private static final Logger log = LoggerFactory.getLogger(S3Service.class);
    private final S3Client s3Client;
    private static final String BUCKET = "cognitia-videos";
    private static final String DEFAULT_URL = "https://" + BUCKET + ".s3.us-east-1.amazonaws.com/";

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void uploadFileToBucket(Path video, Long courseId){
        try {
            File[] files = video.toFile().listFiles();

            if (files == null || files.length == 0) {
                log.warn("No files found in folder: " + video.toString());
                return;
            }

            for (File file : files) {
                if (file.isFile()) {
                    String key = "public/" + courseId + "/" + video.getFileName() + file.getName();

                    PutObjectRequest put = PutObjectRequest.builder()
                            .bucket(BUCKET)
                            .contentType(Files.probeContentType(file.toPath()))
                            .key(key)
                            .build();

                    s3Client.putObject(put, RequestBody.fromFile(file));

                    log.info("Uploaded file to S3: " + key);
                }
            }
            log.info("Video chunks uploaded with sucess");
        } catch (Exception e) {
            log.error("Error while uploading video files to bucket: " + e.getMessage(), e);
    }
}

    public List<String> getVideoUrlsFromCourse(Long courseId){
        List<String> videoUrls = new ArrayList<>();
        try{
            ListObjectsV2Request objectsListRequest = ListObjectsV2Request.builder()
                    .bucket(BUCKET)
                    .prefix("public/"+ courseId)
                    .build();

            ListObjectsV2Response objectsListResponse = s3Client.listObjectsV2(objectsListRequest);

            List<S3Object> imagesList = objectsListResponse.contents();

            for (S3Object s3Object : imagesList) {
                videoUrls.add(DEFAULT_URL + s3Object.key());
            }


        }catch (Exception e){
            log.error("Error while getting a course videos " + e.getMessage());
        }
        return videoUrls;
    }

    public String getVideoUrl(String prefix){
        return DEFAULT_URL + prefix;
    }

    public void deleteObjectFromBucket(String objectKey){
        try{
            DeleteObjectRequest delete = DeleteObjectRequest.builder()
                    .bucket(BUCKET)
                    .key("public/"  + objectKey)
                    .build();

            s3Client.deleteObject(delete);
        }catch (Exception e){
            log.error("Error deleting the object " + e.getMessage());
        }
    }


}
