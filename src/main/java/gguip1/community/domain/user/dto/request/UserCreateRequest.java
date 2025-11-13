package gguip1.community.domain.user.dto.request;

import gguip1.community.global.validation.EmailMax247;
import gguip1.community.global.validation.NicknameValidation;
import gguip1.community.global.validation.StrongPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @EmailMax247
        String email,
        @StrongPassword
        String password,
        String password2,
        @NotBlank
        @Size(min = 1, max = 30)
        @NicknameValidation
        String nickname,
        Long profileImageId
) {
}
