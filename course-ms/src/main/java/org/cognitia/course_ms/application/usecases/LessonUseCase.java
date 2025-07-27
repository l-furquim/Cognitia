package org.cognitia.course_ms.application.usecases;

import org.cognitia.course_ms.application.gateways.S3Gateway;
import org.cognitia.course_ms.application.gateways.LessonGateway;
import org.cognitia.course_ms.domain.lesson.dto.GetCourseLessonsResponse;
import org.cognitia.course_ms.domain.lesson.exceptions.InvalidCourseLessonsRequestException;
import org.cognitia.video_ms.application.gateways.S3Gateway;
import org.cognitia.video_ms.application.gateways.VideoGateway;
import org.cognitia.video_ms.domain.exceptions.*;
import org.cognitia.video_ms.domain.model.Video;
import org.cognitia.video_ms.infra.dto.video.*;
import org.cognitia.video_ms.utils.VideoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class LessonUseCase {

    private static final Logger log = LoggerFactory.getLogger(LessonUseCase.class);
    private final LessonGateway lessonGateway;
    private final S3Gateway s3Gateway;

    public LessonUseCase
            (
            LessonGateway lessonGateway,
            S3Gateway s3Gateway
            ){
        this.lessonGateway = lessonGateway;
        this.s3Gateway = s3Gateway;
    }

    @Cacheable(value="videos", key = "#courseId")
    public GetCourseLessonsResponse getCourseLessons(Long courseId){
        if(courseId == null){
            throw new InvalidCourseLessonsRequestException("Invalid curse id for searching the videos");
        }

        var videos = lessonGateway.getByCourseId(courseId);

        return new GetCourseVideosResponse(videos);
    }

    public void deleteVideo(DeleteVideoRequestDto deleteVideoRequestDto){
        if(deleteVideoRequestDto.videoId() == null){
            throw new InvalidVideoDeletionException("Cannot delete a video with a null id");
        }    private static final String TOPIC = "path.add";


        lessonGateway.delete(deleteVideoRequestDto);
    }
    public Video updateVideo(UpdateVideoMetadataRequest request){
        if(request.videoId() == null){
            throw new InvalidVideoUpdateException("Video id is null, cannot update");
        }

        return lessonGateway.update(request);
    }

    public List<Video> getPathVideos(Long pathId){
        if(pathId == null){
            throw new InvalidGetPathVideosRequest("Path id is null, cannot get videos");
        }

        return lessonGateway.getByPath(pathId);
    }

}
