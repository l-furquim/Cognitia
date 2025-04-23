package org.cognitia.video_ms.application.usecases;

import org.cognitia.video_ms.application.gateways.S3Gateway;
import org.cognitia.video_ms.application.gateways.VideoGateway;
import org.cognitia.video_ms.domain.entity.Video;
import org.cognitia.video_ms.domain.exceptions.InvalidVideoThumbUploadException;
import org.cognitia.video_ms.domain.exceptions.InvalidVideoUploadException;
import org.cognitia.video_ms.domain.exceptions.VideoConvertionException;
import org.cognitia.video_ms.domain.exceptions.VideoNotFoundException;
import org.cognitia.video_ms.infra.dto.video.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VideoUseCase {

    private static final Logger log = LoggerFactory.getLogger(VideoUseCase.class);
    private final VideoGateway videoGateway;
    private final S3Gateway s3Gateway;

    public VideoUseCase
            (
            VideoGateway videoGateway,
            S3Gateway s3Gateway
    ){
        this.videoGateway = videoGateway;
        this.s3Gateway = s3Gateway;
    }

    public UploadVideoResponse uploadVideo(UploadVideoRequest uploadVideoRequest){
        VideoMetadataDto metadata = uploadVideoRequest.metadata();

        if(
                metadata.courseId() == null || metadata.courseId() < 0 ||
                metadata.title() == null || metadata.title().isEmpty() ||
                metadata.description() == null || metadata.description().isEmpty() ||
                metadata.path() == null || metadata.skill() == null || metadata.skill().isEmpty()
        ){
            throw new InvalidVideoUploadException("Invalid data for video upload");
        }

//        if(!uploadVideoRequest.video().getContentType().equals(".mp4")){
//            throw new InvalidVideoContentTypeException("Invalid video extension, we only support mp4");
//        }

        var videoPath = convertToFile(uploadVideoRequest.video());

        Double duration = getVideoDurationInMinutes(videoPath);


        log.info("Video path atual " + videoPath);
        log.info("Duracao atual " + duration);

        new Thread(() -> {
            var mpegPath = convertToMpegDash(videoPath.toPath());

            log.info("Video converted to mpeg dash " + mpegPath);

            s3Gateway.uploadVideoToBucket(mpegPath, uploadVideoRequest.metadata().courseId(), uploadVideoRequest.video().getOriginalFilename());
        }).start();

        var url = s3Gateway.getUrlByPrefix(metadata.courseId() + "/" + uploadVideoRequest.video().getOriginalFilename());

        var video = new Video(
                metadata.title(),
                metadata.description(),
                uploadVideoRequest.video().getOriginalFilename(),
                metadata.path(),
                metadata.skill(),
                duration,
                url,
                null,
                metadata.courseId()
        );

        new Thread(() -> {
            videoGateway.upload(video);
        }).start();

        return new UploadVideoResponse(video);
    }

    public UploadVideoThumbResponse uploadVideoThumb(UploadVideoThumbRequest request){
        var metadata = request.metadata();
        var originalName = request.image().getOriginalFilename();

        if(metadata.videoId() == null || metadata.userId() == null){
            throw new InvalidVideoThumbUploadException("Invalid data while uploading the video thumb");
        }

        var video = videoGateway.findById(metadata.videoId());

        if(video == null){
            throw new VideoNotFoundException("Could not found the video while uploading his thumb");
        }

        var thumbUrl = s3Gateway.getUrlByPrefix(video.courseId() + "/" + video.originalName() + "/" + request.image().getOriginalFilename());

        new Thread(() -> {
            s3Gateway.uploadThumbToBucket(
                    request.image(),
                    video.courseId(),
                    video.originalName()
            );

            videoGateway.uploadThumb(metadata.videoId(), thumbUrl);

        }).start();

        return new UploadVideoThumbResponse(thumbUrl);
    }

    private Path convertToMpegDash(Path path){
        try {
            Path outputDir = Files.createTempDirectory(path.getFileName().toString());
            Path manifestPath = outputDir.resolve("manifest.mpd");

            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg",
                    "-i", path.toString(),
                    "-map", "0",
                    "-f", "dash",
                    "-seg_duration", "4",
                    "-use_timeline", "1",
                    "-use_template", "1",
                    "-adaptation_sets", "id=0,streams=v id=1,streams=a",
                    manifestPath.toString()
            );

            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // use log.info se preferir
            }

            int exitCode = process.waitFor();
            if (exitCode != 0 || !Files.exists(manifestPath)) {
                throw new VideoConvertionException("Falha ao converter vídeo. Código de saída: " + exitCode);
            }

            return outputDir;

        } catch (Exception e) {
            throw new VideoConvertionException("Erro ao converter vídeo para MPEG-DASH: " + e.getMessage());
        }
    }


    private File convertToFile(MultipartFile video){
        File tempFile = null;

        try{
            tempFile = File.createTempFile("video-", ".mp4");
            FileOutputStream fos = new FileOutputStream(tempFile);

            fos.write(video.getBytes());

        }catch (IOException e){
            log.error("Error while converting a multipart file to a temp file " + e.getMessage());
        }
        return tempFile;
    }

    public static Double getVideoDurationInMinutes(File videoFile) {
        try {
            ProcessBuilder builder = new ProcessBuilder(
                    "ffmpeg",
                    "-i", videoFile.getAbsolutePath()
            );
            builder.redirectErrorStream(true); // FFmpeg manda tudo pra stderr

            Process process = builder.start();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {

                String line;

                Pattern durationPattern = Pattern.compile("Duration: (\\d+):(\\d+):(\\d+\\.\\d+)"); // Regex pra buscar a duração retornada pelo ffmpeg

                while ((line = reader.readLine()) != null) {
                    Matcher matcher = durationPattern.matcher(line);
                    if (matcher.find()) {

                        int hours = Integer.parseInt(matcher.group(1));
                        int minutes = Integer.parseInt(matcher.group(2));
                        double seconds = Double.parseDouble(matcher.group(3));

                        double totalSeconds = hours * 3600 + minutes * 60 + seconds;
                        return totalSeconds / 60.0;
                    }
                }
            }
            process.waitFor();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter duração do vídeo com ffmpeg", e);
        }

        throw new RuntimeException("Duração do vídeo não encontrada");
    }

}
