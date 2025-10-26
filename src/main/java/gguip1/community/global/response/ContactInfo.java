package gguip1.community.global.response;

import lombok.Builder;

@Builder
public record ContactInfo(String email, String phone) {
}
