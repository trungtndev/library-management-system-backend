package com.example.springbootweb.controller;

import com.example.springbootweb.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class FileStorageController {
    FileStorageService fileStorageService;

    @GetMapping("/files/{fileId}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String fileId) throws IOException {
        Resource file = fileStorageService.serveFile(fileId);
//        if (!file.exists() || !file.isReadable()) {
//            return ResponseEntity.notFound().build();
//        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}