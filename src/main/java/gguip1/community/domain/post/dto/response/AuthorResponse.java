package gguip1.community.domain.post.dto.response;

import lombok.Builder;

@Builder
public record AuthorResponse(
        String nickname,
        String profileImageUrl
) {
}
