package gguip1.community.domain.user.dto.request;

import gguip1.community.global.validation.NicknameValidation;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @NicknameValidation
        @Size(max = 30)
        String nickname,
        Long profileImageId) {
}
