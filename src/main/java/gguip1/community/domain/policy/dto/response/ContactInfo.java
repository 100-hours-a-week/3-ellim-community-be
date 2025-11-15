package gguip1.community.domain.policy.dto.response;

import lombok.Builder;

@Builder
public record ContactInfo(String email, String phone) {
}
