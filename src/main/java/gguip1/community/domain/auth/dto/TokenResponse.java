package gguip1.community.domain.auth.dto;

import lombok.Builder;

@Builder
public record TokenResponse(String accessToken) {
}
