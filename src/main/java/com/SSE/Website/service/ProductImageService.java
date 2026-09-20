package com.SSE.Website.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductImageService {

    private final Path uploadPath =
            Paths.get("uploads/products");

    public String uploadImage(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Image file is required");
        }

        try {

            // Create folder if it does not exist
            Files.createDirectories(uploadPath);

            String originalFileName =
                    file.getOriginalFilename();

            String extension = "";

            if (originalFileName != null
                    && originalFileName.contains(".")) {

                extension =
                        originalFileName.substring(
                                originalFileName.lastIndexOf(".")
                        );
            }

            String fileName =
                    UUID.randomUUID().toString()
                    + extension;

            Path filePath =
                    uploadPath.resolve(fileName);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return "/uploads/products/" + fileName;

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to upload image",
                    e
            );
        }
    }
}