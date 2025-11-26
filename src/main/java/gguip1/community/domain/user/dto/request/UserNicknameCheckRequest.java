package gguip1.community.domain.user.dto.request;

import gguip1.community.global.validation.NicknameValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserNicknameCheckRequest(
        @NicknameValidation
        @NotBlank
        @Size(min = 1, max = 30)
        String nickname
) {
}
