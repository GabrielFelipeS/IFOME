package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.infra.WebConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class FileStorageService {

    public String storeFile(String cnpj, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        File directory = new File(WebConfig.PATH_TO_FOLDER_IMAGES);
        if (!directory.exists()) {
            directory.mkdirs(); // Cria o diretório
        }

        String newName = this.refactoreName(file.getOriginalFilename());
        File uploadedFile = new File( WebConfig.PATH_TO_FOLDER_IMAGES  + "/" + newName);

        file.transferTo(uploadedFile);
        return uploadedFile.getName(); // Retorna o caminho absoluto do arquivo salvo
    }

    private String refactoreName(String nameImage) {
        if (nameImage == null) {
            throw new RuntimeException("Nome do arquivo é nulo");
        }
        String extension = nameImage.substring(nameImage.lastIndexOf('.'));

        Random random = new Random();
        int randomNumber1 = random.nextInt(1001);
        int randomNumber2 = random.nextInt(1001);
        char randomLetter = (char) ('A' + random.nextInt(26));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String dateTimeNow = LocalDateTime.now().format(formatter);
        return randomNumber1 + "-" + dateTimeNow + "-" + randomNumber2 + "-" + randomLetter + extension;
    }

}