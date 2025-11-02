package gguip1.community.domain.user.dto.request;

import gguip1.community.global.validation.EmailMax247;
import gguip1.community.global.validation.NicknameNoWhitespace;
import gguip1.community.global.validation.StrongPassword;

public record UserCreateRequest(
        @EmailMax247
        String email,
        @StrongPassword
        String password,
        String password2,
        @NicknameNoWhitespace
        String nickname,
        Long profileImageId
) {
}
