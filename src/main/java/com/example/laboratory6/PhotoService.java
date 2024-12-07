package com.example.laboratory6;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PhotoService {

    private final String uploadDir = "uploads"; // Папка для загрузки фотографий

    public void savePhoto(MultipartFile file, String username) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filename = username + "_" + file.getOriginalFilename(); // Уникальное имя файла
        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, file.getBytes());
    }

    public String getPhotoPathByUsername(String username) {
        return uploadDir + "/" + username + "_profile.jpg"; // Пример пути
    }
}
