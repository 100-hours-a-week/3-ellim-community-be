package gguip1.community.domain.user.dto.request;

import gguip1.community.global.validation.NicknameNoWhitespace;
import lombok.Builder;

@Builder
public record UserNicknameCheckRequest(
        @NicknameNoWhitespace
        String nickname
) {
}
