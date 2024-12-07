package com.example.laboratory6;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/images")
public class ImageController {

    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    @GetMapping("/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) throws IOException {
        Path imagePath = Paths.get(UPLOAD_DIR + imageName);
        Resource image = new UrlResource(imagePath.toUri());

        if (!image.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(image);
    }
}
