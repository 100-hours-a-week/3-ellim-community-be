package gguip1.community.domain.user.dto.request;

import gguip1.community.global.validation.NicknameNoWhitespace;

public record UserUpdateRequest(
        @NicknameNoWhitespace
        String nickname,
        Long profileImageId) {
}
