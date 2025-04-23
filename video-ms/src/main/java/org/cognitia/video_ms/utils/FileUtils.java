package org.cognitia.video_ms.utils;

import lombok.extern.slf4j.Slf4j;
import org.cognitia.video_ms.domain.exceptions.VideoConvertionException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class FileUtils {

    public Path convertToMpegDash(Path path){
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


    public File convertToFile(MultipartFile video){
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
