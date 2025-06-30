package com.ytvdownloader.videodownloader.service;

import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLRequest;
import com.sapher.youtubedl.YoutubeDLResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
@Slf4j
public class DownloadService {
    @Getter
//    @Value("${download.directory:./downloads}")
    private final String downloadDir = "./Downloads/";

    public String downloadVideo(String youtubeUrl) throws Exception {
        log.info("Starting video download for URL: {}", youtubeUrl);

        // Validate URL
        if (!youtubeUrl.contains("youtube.com/watch?v=")) {
            throw new IllegalArgumentException("Invalid YouTube URL: " + youtubeUrl);
        }

        String ytDlpPath = "C:\\Program Files\\Python313\\Scripts\\yt-dlp.exe";
        File ytDlpFile = new File(ytDlpPath);
        if (!ytDlpFile.exists()) {
            throw new IllegalStateException("yt-dlp executable not found at: " + ytDlpPath);
        }
        YoutubeDL.setExecutablePath(ytDlpPath);

        // Configure youtube-dl request
        YoutubeDLRequest request = new YoutubeDLRequest(youtubeUrl, downloadDir);
        request.setOption("output", "%(title)s.%(ext)s");
        request.setOption("format", "bestvideo[ext=mp4]+bestaudio[ext=m4a]/best[ext=mp4]/best");

        // Execute download
        YoutubeDLResponse response;
        try {
            response = YoutubeDL.execute(request);
            log.info("Download completed: {}", response.getOut());
        } catch (Exception e) {
            log.error("Download failed: {}", e.getMessage());
            throw e;
        }

        // Find the downloaded file
        String fileName = response.getOut().lines()
                .filter(line -> line.contains("[download] Destination:"))
                .map(line -> line.split("Destination: ")[1])
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not determine output file"));
        File outputFile = new File(downloadDir, fileName);
        return outputFile.getAbsolutePath();
    }
}
