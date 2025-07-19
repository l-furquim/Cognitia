package org.cognitia.video_ms.infra.s3;

import org.cognitia.video_ms.application.gateways.S3Gateway;
import org.cognitia.video_ms.domain.exceptions.VideoConversionException;
import org.cognitia.video_ms.infra.dto.video.VideoProcessingResult;
import org.cognitia.video_ms.infra.dto.video.VideoS3Structure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public void uploadVideoProcessingResult(VideoProcessingResult result, Long courseId, String videoName) {
        try {
            String basePrefix = "public/" + courseId + "/" + videoName + "/";

            // Upload thumbnail to root level
            if (Files.exists(result.thumbnail())) {
                uploadFileToS3(result.thumbnail().toFile(), basePrefix + "thumbnail.jpg");
            }

            // Upload master manifest to root level
            if (Files.exists(result.masterManifest())) {
                uploadFileToS3(result.masterManifest().toFile(), basePrefix + "master.m3u8");
            }

            // Upload each quality directory
            for (Map.Entry<String, Path> qualityEntry : result.qualityDirectories().entrySet()) {
                String quality = qualityEntry.getKey();
                Path qualityDir = qualityEntry.getValue();

                uploadQualityDirectory(qualityDir, basePrefix + quality + "/");
            }

            log.info("Successfully uploaded video with multiple qualities for course {} video {}", courseId, videoName);

        } catch (Exception e) {
            log.error("Error uploading video processing result to S3: {}", e.getMessage(), e);
            throw new VideoConversionException("Failed to upload video to S3: " + e.getMessage());
        }
    }

    @Override
    public void uploadVideoToBucket(Path video, Long courseId, String videoName) {
        try {
            File[] files = video.toFile().listFiles();

            if (files == null || files.length == 0) {
                log.warn("No files found in folder: {}", video);
                return;
            }

            for (File file : files) {
                if (file.isFile()) {
                    String key = "public/" + courseId + "/" + videoName + "/" + file.getName();
                    uploadFileToS3(file, key);
                }
            }
            log.info("Video chunks uploaded successfully");
        } catch (Exception e) {
            log.error("Error while uploading video files to bucket: {}", e.getMessage(), e);
        }
    }

    @Override
    public void uploadThumbToBucket(MultipartFile thumb, Long courseId, String videoName) {
        try {
            String key = "public/" + courseId + "/" + videoName + "/thumbnail.jpg";

            PutObjectRequest put = PutObjectRequest.builder()
                    .bucket(BUCKET)
                    .contentType(thumb.getContentType())
                    .contentLength(thumb.getSize())
                    .key(key)
                    .build();

            s3Client.putObject(put, RequestBody.fromBytes(thumb.getBytes()));
            log.info("Uploaded thumbnail to S3: {}", key);

        } catch (IOException e) {
            log.error("Error while uploading video thumbnail: {}", e.getMessage(), e);
        }
    }

    public List<String> getVideoUrlsFromCourse(Long courseId) {
        List<String> videoUrls = new ArrayList<>();
        try {
            ListObjectsV2Request objectsListRequest = ListObjectsV2Request.builder()
                    .bucket(BUCKET)
                    .prefix("public/" + courseId)
                    .build();

            ListObjectsV2Response objectsListResponse = s3Client.listObjectsV2(objectsListRequest);
            List<S3Object> imagesList = objectsListResponse.contents();

            for (S3Object s3Object : imagesList) {
                videoUrls.add(DEFAULT_URL + s3Object.key());
            }

        } catch (Exception e) {
            log.error("Error while getting course videos: {}", e.getMessage(), e);
        }
        return videoUrls;
    }

    public VideoS3Structure getVideoStructure(Long courseId, String videoName) {
        try {
            String prefix = "public/" + courseId + "/" + videoName + "/";

            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(BUCKET)
                    .prefix(prefix)
                    .build();

            ListObjectsV2Response response = s3Client.listObjectsV2(request);

            Map<String, List<String>> qualityFiles = new HashMap<>();
            List<String> rootFiles = new ArrayList<>();

            for (S3Object s3Object : response.contents()) {
                String key = s3Object.key();
                String relativePath = key.substring(prefix.length());

                if (relativePath.contains("/")) {
                    // File is in a quality subdirectory
                    String quality = relativePath.substring(0, relativePath.indexOf("/"));
                    qualityFiles.computeIfAbsent(quality, k -> new ArrayList<>())
                            .add(DEFAULT_URL + key);
                } else {
                    // File is at root level (thumbnail, master manifest)
                    rootFiles.add(DEFAULT_URL + key);
                }
            }

            return new VideoS3Structure(
                    DEFAULT_URL + prefix + "master.m3u8",
                    DEFAULT_URL + prefix + "thumbnail.jpg",
                    qualityFiles,
                    rootFiles
            );

        } catch (Exception e) {
            log.error("Error getting video structure for course {} video {}: {}",
                    courseId, videoName, e.getMessage(), e);
            return new VideoS3Structure("", "", Map.of(), List.of());
        }
    }

    public String getUrlByPrefix(String prefix) {
        return DEFAULT_URL + "public/" + prefix;
    }

    @Override
    public void deleteObjectFromBucket(String objectKey) {
        try {
            DeleteObjectRequest delete = DeleteObjectRequest.builder()
                    .bucket(BUCKET)
                    .key("public/" + objectKey)
                    .build();

            s3Client.deleteObject(delete);
            log.info("Deleted object from S3: public/{}", objectKey);
        } catch (Exception e) {
            log.error("Error deleting object: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteVideoDirectory(Long courseId, String videoName) {
        try {
            String prefix = "public/" + courseId + "/" + videoName + "/";

            ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                    .bucket(BUCKET)
                    .prefix(prefix)
                    .build();

            ListObjectsV2Response listResponse;
            do {
                listResponse = s3Client.listObjectsV2(listRequest);

                if (!listResponse.contents().isEmpty()) {
                    List<ObjectIdentifier> objectsToDelete = listResponse.contents().stream()
                            .map(s3Object -> ObjectIdentifier.builder().key(s3Object.key()).build())
                            .collect(Collectors.toList());

                    DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
                            .bucket(BUCKET)
                            .delete(Delete.builder().objects(objectsToDelete).build())
                            .build();

                    s3Client.deleteObjects(deleteRequest);
                }

                listRequest = listRequest.toBuilder()
                        .continuationToken(listResponse.nextContinuationToken())
                        .build();

            } while (listResponse.isTruncated());

            log.info("Deleted video directory: {}", prefix);

        } catch (Exception e) {
            log.error("Error deleting video directory for course {} video {}: {}",
                    courseId, videoName, e.getMessage(), e);
        }
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