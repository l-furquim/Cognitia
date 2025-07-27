package org.cognitia.encoder.infra.s3;

import org.cognitia.encoder.application.gateways.S3Gateway;
import org.cognitia.encoder.domain.exceptions.VideoConversionException;
import org.cognitia.encoder.domain.exceptions.VideoUploadException;
import org.cognitia.encoder.infra.dto.video.VideoProcessingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Service
public class S3Service implements S3Gateway {

    private static final Logger log = LoggerFactory.getLogger(S3Service.class);
    private final S3Client s3Client;
    private static final String BUCKET = "cognitia-videos";
    private static final String DEFAULT_URL = "https://" + BUCKET + ".s3.us-east-1.amazonaws.com/";

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public void uploadVideoProcessingResult(VideoProcessingResult result, Long courseId, String lessonName) {
        try {
            String basePrefix = "public/" + courseId + "/" + lessonName + "/";

            // Upload thumbnail to root level
            if (Files.exists(result.thumbnail())) {
                uploadFileToS3(result.thumbnail().toFile(), basePrefix + "thumbnail.jpg");
                result.thumbnail().toFile().delete();
            }

            // Upload master manifest to root level
            if (Files.exists(result.masterManifest())) {
                uploadFileToS3(result.masterManifest().toFile(), basePrefix + "master.m3u8");
                result.masterManifest().toFile().delete();
            }

            // Upload each quality directory
            for (Map.Entry<String, Path> qualityEntry : result.qualityDirectories().entrySet()) {
                String quality = qualityEntry.getKey();
                Path qualityDir = qualityEntry.getValue();

                uploadQualityDirectory(qualityDir, basePrefix + quality + "/");

                qualityDir.toFile().delete();
            }

            log.info("Successfully uploaded video with multiple qualities for course {} video {}", courseId, result.baseOutputDir());

        } catch (Exception e) {
            log.error("Error uploading video processing result to S3: {}", e.getMessage(), e);
            throw new VideoConversionException("Failed to upload video to S3: " + e.getMessage());
        }
    }

    @Override
    public File downloadS3RawVideoToTempFile(String lessonName,String videoName ,Long courseId, File tempFile) {
        try {

            String key = "public/" + courseId + "/" + lessonName + "/" + videoName;

            log.info("Downloading the current video from s3: {}", key);

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(BUCKET)
                    .key(key)
                    .build();

            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                s3Client.getObject(getObjectRequest, software.amazon.awssdk.core.sync.ResponseTransformer.toOutputStream(fos));
            }

            log.debug("Arquivo temporário criado: {}", tempFile.getAbsolutePath());

            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException("Erro criando arquivo temporário: " + e.getMessage(), e);
        }
    }


    @Override
    public void uploadVideoToBucket(MultipartFile video, Long courseId, String lessonName) {
        try {
            String contentType = video.getContentType();

            String key = "public/" + courseId + "/" + lessonName + "/" + video.getOriginalFilename();

            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(BUCKET)
                    .contentType(contentType)
                    .key(key)
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromInputStream(video.getInputStream(), video.getSize()));

            log.info("Uploaded file to S3: {}", key);
        } catch (Exception e) {
            throw new VideoUploadException("Error while uploading the raw video to the bucket: " + e.getMessage());
        }
    }

    public String getUrlByPrefix(String prefix) {
        return DEFAULT_URL + "public/" + prefix;
    }

    private void uploadQualityDirectory(Path qualityDir, String s3Prefix) {
        try {
            File[] files = qualityDir.toFile().listFiles();

            if (files == null || files.length == 0) {
                log.warn("No files found in quality directory: {}", qualityDir);
                return;
            }

            for (File file : files) {
                if (file.isFile()) {
                    String key = s3Prefix + file.getName();
                    uploadFileToS3(file, key);
                    file.delete();
                }
            }

        } catch (Exception e) {
            log.error("Error uploading quality directory {}: {}", qualityDir, e.getMessage(), e);
        }
    }

    private void uploadFileToS3(File file, String key) {
        try {
            String contentType = Files.probeContentType(file.toPath());
            if (contentType == null) {
                contentType = determineContentType(file.getName());
            }

            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(BUCKET)
                    .contentType(contentType)
                    .key(key)
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromFile(file));
            log.debug("Uploaded file to S3: {}", key);

        } catch (Exception e) {
            log.error("Error uploading file {} to S3: {}", file.getName(), e.getMessage(), e);
        }
    }

    private String determineContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

        return switch (extension) {
            case "mpd" -> "application/dash+xml";
            case "m4s" -> "video/iso.segment";
            case "m3u8" -> "application/vnd.apple.mpegurl";
            case "ts" -> "video/mp2t";
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "webp" -> "image/webp";
            default -> "application/octet-stream";
        };
    }
}