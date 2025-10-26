package gguip1.community.domain.image.mapper;

import gguip1.community.domain.image.entity.Image;
import org.springframework.stereotype.Component;

@Component
public class ImageMapper {
    public Image toEntity(String url) {
        return Image.builder()
                .url(url)
                .build();
    }
}
