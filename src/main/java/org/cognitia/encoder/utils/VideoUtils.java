package org.cognitia.encoder.utils;

import lombok.extern.slf4j.Slf4j;
import org.cognitia.encoder.domain.exceptions.TempRawVideoException;
import org.cognitia.encoder.domain.exceptions.VideoConversionException;
import org.cognitia.encoder.infra.dto.video.VideoProcessingResult;
import org.cognitia.encoder.infra.dto.video.VideoQualityConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class VideoUtils {

    private static final String DASH_SEGMENT_DURATION = "4";
    private static final Pattern DURATION_PATTERN = Pattern.compile("Duration: (\\d+):(\\d+):(\\d+(?:\\.\\d+)?)");

    private static final Map<String, VideoQualityConfig> QUALITY_CONFIGS = Map.of(
            "420p", new VideoQualityConfig("420p", "854x480", "800k", "128k"),
            "720p", new VideoQualityConfig("720p", "1280x720", "2500k", "192k"),
            "1080p", new VideoQualityConfig("1080p", "1920x1080", "5000k", "256k")
    );

    public VideoProcessingResult convertToMpegDashWithQualities(Path inputPath) {
        validateInputFile(inputPath);

        try {
            Path baseOutputDir = createBaseOutputDirectory(inputPath);
            Path thumbnailPath = generateThumbnail(inputPath, baseOutputDir);

            Map<String, Path> qualityDirs = new HashMap<>();

            for (Map.Entry<String, VideoQualityConfig> entry : QUALITY_CONFIGS.entrySet()) {
                String quality = entry.getKey();
                VideoQualityConfig config = entry.getValue();

                Path qualityDir = baseOutputDir.resolve(quality);
                Files.createDirectories(qualityDir);

                convertToQuality(inputPath, qualityDir, config);
                qualityDirs.put(quality, qualityDir);

                log.info("Successfully converted video to {}: {}", quality, qualityDir);
            }

            Path masterManifest = generateMasterManifest(baseOutputDir, qualityDirs);

            return new VideoProcessingResult(baseOutputDir, qualityDirs, masterManifest, thumbnailPath);

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new VideoConversionException("Failed to convert video to MPEG-DASH with multiple qualities: "+  e.getMessage());
        }
    }

    public File createSimpleTempFile(String videoName) {
        try {
            String fileExtension = extractFileExtension(videoName);

            File tempFile = File.createTempFile(videoName, fileExtension);

            log.info("A: {}",tempFile.getName());

            log.debug("Arquivo simples temporario criado: {}", tempFile.getAbsolutePath());

            return tempFile;
        } catch (IOException e) {
            throw new TempRawVideoException("Error while creating the temp file for the raw video: " + e.getMessage());
        }
    }

    public static Double getVideoDurationInMinutes(File videoFile) {
        validateVideoFile(videoFile);

        try {
            ProcessBuilder builder = new ProcessBuilder(
                    "ffmpeg",
                    "-i", videoFile.getAbsolutePath(),
                    "-hide_banner"
            );
            builder.redirectErrorStream(true);

            Process process = builder.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {

                return parseDurationFromOutput(reader);

            } finally {
                process.waitFor();
            }

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new VideoConversionException("Failed to get video duration: " + e.getMessage());
        }
    }

    private Path createBaseOutputDirectory(Path inputPath) throws IOException {
        String fileName = inputPath.getFileName().toString();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        return Files.createTempDirectory("video-" + baseName + "-");
    }

    private Path generateThumbnail(Path inputPath, Path outputDir) throws IOException, InterruptedException {
        Path thumbnailPath = outputDir.resolve("thumbnail.jpg");

        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffmpeg",
                "-i", inputPath.toString(),
                "-ss", "00:00:05",
                "-vframes", "1",
                "-q:v", "2",
                "-y",
                thumbnailPath.toString()
        );

        Process process = processBuilder.start();
        handleProcessOutput(process);

        int exitCode = process.waitFor();
        if (exitCode != 0 || !Files.exists(thumbnailPath)) {
            log.warn("Failed to generate thumbnail, creating default");
        }

        return thumbnailPath;
    }

    private void convertToQuality(Path inputPath, Path qualityDir, VideoQualityConfig config)
            throws IOException, InterruptedException {

        Path manifestPath = qualityDir.resolve("manifest.mpd");

        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffmpeg",
                "-i", inputPath.toString(),
                "-c:v", "libx264",
                "-c:a", "aac",
                "-s", config.resolution(),
                "-b:v", config.videoBitrate(),
                "-b:a", config.audioBitrate(),
                "-f", "dash",
                "-seg_duration", DASH_SEGMENT_DURATION,
                "-use_timeline", "1",
                "-use_template", "1",
                "-adaptation_sets", "id=0,streams=v id=1,streams=a",
                "-hide_banner",
                "-loglevel", "info",
                "-y",
                manifestPath.toString()
        );

        Process process = processBuilder.start();
        handleProcessOutput(process);

        int exitCode = process.waitFor();
        validateConversionResult(exitCode, manifestPath);
    }

    private Path generateMasterManifest(Path baseDir, Map<String, Path> qualityDirs) throws IOException {
        Path masterManifest = baseDir.resolve("master.m3u8");

        try (BufferedWriter writer = Files.newBufferedWriter(masterManifest)) {
            writer.write("#EXTM3U\n");
            writer.write("#EXT-X-VERSION:3\n\n");

            for (Map.Entry<String, String> entry : Map.of(
                    "420p", "#EXT-X-STREAM-INF:BANDWIDTH=928000,RESOLUTION=854x480",
                    "720p", "#EXT-X-STREAM-INF:BANDWIDTH=2692000,RESOLUTION=1280x720",
                    "1080p", "#EXT-X-STREAM-INF:BANDWIDTH=5256000,RESOLUTION=1920x1080"
            ).entrySet()) {

                String quality = entry.getKey();
                String streamInfo = entry.getValue();

                if (qualityDirs.containsKey(quality)) {
                    writer.write(streamInfo + "\n");
                    writer.write(quality + "/manifest.mpd\n\n");
                }
            }
        }

        return masterManifest;
    }

    private void validateInputFile(Path inputPath) {
        if (inputPath == null) {
            throw new IllegalArgumentException("Input path cannot be null");
        }
        if (!Files.exists(inputPath)) {
            throw new VideoConversionException("Input file does not exist: " + inputPath);
        }
        if (!Files.isReadable(inputPath)) {
            throw new VideoConversionException("Input file is not readable: " + inputPath);
        }
    }

    private void validateMultipartFile(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("MultipartFile cannot be null or empty");
        }
    }

    private static void validateVideoFile(File videoFile) {
        if (videoFile == null || !videoFile.exists() || !videoFile.canRead()) {
            throw new IllegalArgumentException("Video file must exist and be readable");
        }
    }

    private ProcessBuilder createDashConversionProcess(Path inputPath, Path manifestPath) {
        return new ProcessBuilder(
                "ffmpeg",
                "-i", inputPath.toString(),
                "-map", "0",
                "-f", "dash",
                "-seg_duration", DASH_SEGMENT_DURATION,
                "-use_timeline", "1",
                "-use_template", "1",
                "-adaptation_sets", "id=0,streams=v id=1,streams=a",
                "-hide_banner",
                "-loglevel", "info",
                manifestPath.toString()
        );
    }

    private void handleProcessOutput(Process process) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                log.debug("FFmpeg output: {}", line);
            }
        }
    }

    private void validateConversionResult(int exitCode, Path manifestPath) {
        if (exitCode != 0) {
            throw new VideoConversionException("FFmpeg conversion failed with exit code: " + exitCode);
        }
        if (!Files.exists(manifestPath)) {
            throw new VideoConversionException("Manifest file was not created: " + manifestPath);
        }
    }

    private String extractFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ".tmp";
        }
        return filename.substring(filename.lastIndexOf('.'));
    }

    private static Double parseDurationFromOutput(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            Matcher matcher = DURATION_PATTERN.matcher(line);
            if (matcher.find()) {
                int hours = Integer.parseInt(matcher.group(1));
                int minutes = Integer.parseInt(matcher.group(2));
                double seconds = Double.parseDouble(matcher.group(3));

                double totalMinutes = (hours * 60.0) + minutes + (seconds / 60.0);
                return totalMinutes;
            }
        }
        throw new VideoConversionException("Duration not found in FFmpeg output");
    }
}
