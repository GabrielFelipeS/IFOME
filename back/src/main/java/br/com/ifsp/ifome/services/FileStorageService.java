package br.com.ifsp.ifome.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}") // Use a propriedade configurada para o diretório de upload
    private String uploadDir;

    public String storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        // Cria o diretório se não existir
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs(); // Cria o diretório
        }

        // Define o caminho completo do arquivo
        String pathName = uploadDir + file.getOriginalFilename();
        File uploadedFile = new File(pathName);

        // Transferir o arquivo para o diretório especificado
        file.transferTo(uploadedFile);

        return uploadedFile.getAbsolutePath(); // Retorna o caminho absoluto do arquivo salvo
    }
}