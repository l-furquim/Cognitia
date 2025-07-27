package org.cognitia.encoder.infra.dto.video;

import java.nio.file.Path;
import java.util.Map;

public record VideoProcessingResult(
        Path baseOutputDir,
        Map<String, Path> qualityDirectories,
        Path masterManifest,
        Path thumbnail
) {}
