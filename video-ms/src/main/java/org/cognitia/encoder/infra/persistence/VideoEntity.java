package org.cognitia.encoder.infra.persistence;


import org.cognitia.encoder.domain.enums.ProcessStatus;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class VideoEntity {

    public VideoEntity() {
    }

    public VideoEntity(String name, ProcessStatus status, Double durationInMinutes, String extension, String thumbUrl, String playlistsUrl, String playbackUrl, double originalFileSize, String failedReason) {
        this.name = name;
        this.status = status;
        this.durationInMinutes = durationInMinutes;
        this.extension = extension;
        this.thumbUrl = thumbUrl;
        this.playlistsUrl = playlistsUrl;
        this.playbackUrl = playbackUrl;
        this.originalFileSize = originalFileSize;
        this.failedReason = failedReason;
    }

    private String id;

    private String name;

    private ProcessStatus status;

    private Double durationInMinutes;

    private String extension;

    private String thumbUrl;

    private String playlistsUrl;

    private String playbackUrl;

    private double originalFileSize;

    private String failedReason;

    @DynamoDbPartitionKey
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProcessStatus getStatus() {
        return status;
    }

    public void setStatus(ProcessStatus status) {
        this.status = status;
    }

    public Double getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Double durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getPlaylistsUrl() {
        return playlistsUrl;
    }

    public void setPlaylistsUrl(String playlistsUrl) {
        this.playlistsUrl = playlistsUrl;
    }

    public String getPlaybackUrl() {
        return playbackUrl;
    }

    public void setPlaybackUrl(String playbackUrl) {
        this.playbackUrl = playbackUrl;
    }

    public double getOriginalFileSize() {
        return originalFileSize;
    }

    public void setOriginalFileSize(double originalFileSize) {
        this.originalFileSize = originalFileSize;
    }

    public String getFailedReason() {
        return failedReason;
    }

    public void setFailedReason(String failedReason) {
        this.failedReason = failedReason;
    }
}
