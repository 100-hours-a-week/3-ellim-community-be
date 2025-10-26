package gguip1.community.domain.image.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalFileService implements FileService{
    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String uploadDir = System.getProperty("user.dir") + "/upload/images/"; // 로컬 스토리지 저장
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path path = Paths.get(uploadDir + filename); // 파일 경로 생성
        Files.createDirectories(path.getParent()); // 디렉토리 생성
        file.transferTo(path.toFile()); // 파일 저장

        return filename;
    }
}
