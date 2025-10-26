package gguip1.community.domain.image.service;

import gguip1.community.domain.image.dto.ImageResponse;
import gguip1.community.domain.image.entity.Image;
import gguip1.community.domain.image.mapper.ImageMapper;
import gguip1.community.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final FileService fileService;
    private final  ImageCompressor imageCompressor;

    private final ImageMapper imageMapper;
    private final ImageRepository imageRepository;

    public ImageResponse uploadImage(MultipartFile multipartFile) throws IOException {
        MultipartFile compressedImage = imageCompressor.compressImage(multipartFile, 0.7f);
        ImageResponse response = new ImageResponse();

        String imageUrl = "http://localhost:8080/images/" + fileService.uploadFile(compressedImage);
        response.setImageUrl(imageUrl);

        Image uploadImage = imageRepository.save(imageMapper.toEntity(imageUrl));
        response.setImageId(uploadImage.getImageId());

        return response;
    }
}
