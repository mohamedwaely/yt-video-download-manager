package com.ytvdownloader.videodownloader.controller;
import com.ytvdownloader.videodownloader.service.DownloadService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("yt-download/v1/video-download")
@RequiredArgsConstructor
@Slf4j
public class DownloadController {
    private final DownloadService downloadService;

    @PostMapping
    public ResponseEntity<String> downloadVideo(@RequestBody String youtubeUrl) {
        log.info("Received download request for URL: {}", youtubeUrl);
        try {
            String filePath = downloadService.downloadVideo(youtubeUrl);
            log.info("Video downloaded successfully to: {}", filePath);
            return ResponseEntity.ok("Video downloaded to: " + filePath);
        } catch (Exception e) {
            log.error("Failed to download video: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
