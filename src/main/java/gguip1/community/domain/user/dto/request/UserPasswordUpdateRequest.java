package gguip1.community.domain.user.dto.request;

import gguip1.community.global.validation.StrongPassword;

public record UserPasswordUpdateRequest(
        @StrongPassword
        String newPassword,
        String newPassword2
) {
}
