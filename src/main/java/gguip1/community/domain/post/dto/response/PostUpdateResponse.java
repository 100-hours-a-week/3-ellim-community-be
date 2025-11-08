package gguip1.community.domain.post.dto.response;

import lombok.Builder;

@Builder
public record PostUpdateResponse(
        Long postId
) {
}
