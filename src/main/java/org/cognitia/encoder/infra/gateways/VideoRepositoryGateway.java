package org.cognitia.encoder.infra.gateways;

import lombok.extern.slf4j.Slf4j;
import org.cognitia.encoder.application.gateways.VideoGateway;
import org.cognitia.encoder.domain.enums.ProcessStatus;
import org.cognitia.encoder.domain.exceptions.VideoNotFoundException;
import org.cognitia.encoder.domain.exceptions.VideoUploadException;
import org.cognitia.encoder.domain.model.Video;
import org.cognitia.encoder.infra.mappers.VideoMapper;
import org.cognitia.encoder.infra.persistence.VideoEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

@Slf4j
@Component
public class VideoRepositoryGateway implements VideoGateway {

    private final VideoMapper videoMapper;
    private final DynamoDbTable<VideoEntity> dynamoRepository;

    public VideoRepositoryGateway(VideoMapper videoMapper, DynamoDbTable<VideoEntity> dynamoRepository) {
        this.videoMapper = videoMapper;
        this.dynamoRepository = dynamoRepository;
    }

    @Override
    public void upload(Video video) {
        try {
            var videoEntity = videoMapper.toEntity(video);

            log.info("Video entity {}", videoEntity);

            dynamoRepository.putItem(videoEntity);
        } catch (Exception e) {
            log.error("Error uploading the video entity: {}", e.getMessage());

            throw new VideoUploadException("Error uploading the video entity " + e.getMessage());
        }
    }

    @Override
    public void updateVideo(Video video) {
        VideoEntity entityVideo = videoMapper.toEntity(video);

        dynamoRepository.putItem(entityVideo);
    }

    @Override
    public void updateVideoStatus(String videoId, ProcessStatus status, String failedReason) {
        VideoEntity video = dynamoRepository.getItem(Key.builder()
                .partitionValue(videoId)
                .build());

        if (video == null) {
            throw new VideoNotFoundException("Vídeo não encontrado com ID: " + videoId);
        }

        video.setStatus(status);
        video.setFailedReason(failedReason);

        dynamoRepository.updateItem(video);
    }

    @Override
    public void delete(String videoId) {
        Key key = Key.builder()
                .partitionValue(videoId)
                .build();

        dynamoRepository.deleteItem(key);
    }
}
