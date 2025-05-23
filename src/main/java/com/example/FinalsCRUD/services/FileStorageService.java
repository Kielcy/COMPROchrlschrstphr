package com.example.FinalsCRUD.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Service for handling file storage operations.
 */
@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    /**
     * Constructor that initializes the file storage location.
     * Creates the directory if it doesn't exist.
     */
    public FileStorageService() {
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
        
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    /**
     * Stores a file in the file system with a unique name.
     *
     * @param file The file to store.
     * @return The file name as stored in the file system.
     */
    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        
        try {
            // Generate a unique file name to prevent file name collisions
            String originalFileName = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
            }
            String fileName = UUID.randomUUID().toString() + fileExtension;
            
            // Copy the file to the target location
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename(), ex);
        }
    }
    
    /**
     * Gets the complete path to a file.
     * 
     * @param fileName The name of the file.
     * @return The complete path to the file.
     */
    public Path getFilePath(String fileName) {
        return this.fileStorageLocation.resolve(fileName);
    }
} 