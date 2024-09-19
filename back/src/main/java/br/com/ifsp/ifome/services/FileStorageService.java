package br.com.ifsp.ifome.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileStorageService {
    private final String PATH_TO_FOLDER_IMAGES =  "/src/main/resources/static/images/";
    public String storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }
        String pathName = System.getProperty("user.dir") + PATH_TO_FOLDER_IMAGES + file.getOriginalFilename();
        System.out.println(pathName);
        File uploadedFile = new File(pathName);
        file.transferTo(uploadedFile);

        return uploadedFile.getAbsolutePath();
    }
}