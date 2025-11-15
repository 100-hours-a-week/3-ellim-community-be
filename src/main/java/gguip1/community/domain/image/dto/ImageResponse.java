package gguip1.community.domain.image.dto;

import lombok.Builder;

@Builder
public record ImageResponse(Long imageId, String imageUrl) {
}
