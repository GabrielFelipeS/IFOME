package br.com.ifsp.ifome.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileStorageService {

    private final String PATH_TO_FOLDER_IMAGES =  "/src/main/resources/static/images/";

    public String storeFile(String cnpj, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        File directory = new File(PATH_TO_FOLDER_IMAGES);
        if (!directory.exists()) {
            directory.mkdirs(); // Cria o diretório
        }

        String extension = this.extension(file);
        String newName = cnpj.replaceAll("[^0-9]", "") + extension;
        System.err.println(newName);
        File uploadedFile = new File(System.getProperty("user.dir") + PATH_TO_FOLDER_IMAGES + newName);
        System.err.println(uploadedFile);

        // Transferir o arquivo para o diretório especificado
        file.transferTo(uploadedFile);

        return uploadedFile.getName(); // Retorna o caminho absoluto do arquivo salvo
    }

    private String extension(MultipartFile file) {
        String contentType = file.getContentType();
        if(contentType.equals("image/jpeg")) {
            return ".jpg";
        } else {
            return ".png";
        }
    }
}