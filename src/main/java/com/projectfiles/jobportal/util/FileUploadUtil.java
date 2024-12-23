package com.projectfiles.jobportal.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    public static void saveFile(String uploadDir, String filename, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try(InputStream inputStream = multipartFile.getInputStream();) {
            Path path = uploadPath.resolve(filename);
            System.out.println("File Path: " + path);
            System.out.println("File Name: " + filename);
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        } catch(IOException e) {
            throw new IOException("could not save image file: " + filename, e);
        }
    }

}