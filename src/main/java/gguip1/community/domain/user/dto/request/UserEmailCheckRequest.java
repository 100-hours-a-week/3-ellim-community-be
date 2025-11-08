package gguip1.community.domain.user.dto.request;

import gguip1.community.global.validation.EmailMax247;
import lombok.Builder;

@Builder
public record UserEmailCheckRequest(
        @EmailMax247
        String email
) {
}
