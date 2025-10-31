package gguip1.community.domain.auth.dto;

import lombok.Builder;

@Builder
public record AuthResponse(Long userId, String email, String profileImageUrl, String nickname, String accessToken) {
}
