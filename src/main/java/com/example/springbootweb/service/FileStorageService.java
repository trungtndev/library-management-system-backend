package com.example.springbootweb.service;

import com.example.springbootweb.entity.FileStorage;
import com.example.springbootweb.repository.FileStorageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileStorageService {
    Path uploadPath;

    FileStorageRepository fileStorageRepository;

    public FileStorage storeFile(MultipartFile file) throws IOException {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String storedFileName = UUID.randomUUID() + originalFileName;

        Path target = uploadPath.resolve(storedFileName);
        Files.copy(file.getInputStream(), target);

        FileStorage fileStorage = new FileStorage();

        fileStorage.setOriginalFileName(originalFileName);
        fileStorage.setStoredFileName(storedFileName);
        fileStorage.setFilePath(target.toString());

        fileStorage.setSize(file.getSize());
        fileStorage.setContentType(file.getContentType());
        return fileStorageRepository.save(fileStorage);
    }

    public Resource serveFile(String fileId) throws IOException {
        FileStorage fileStorage = fileStorageRepository.findById(fileId)
                .orElseThrow(() -> new IOException("File not found with id: " + fileId));
        Path file = uploadPath.resolve(fileStorage.getFilePath()).normalize();
        return new UrlResource(file.toUri());
    }
}
