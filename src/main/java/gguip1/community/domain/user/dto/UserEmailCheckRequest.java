package gguip1.community.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserEmailCheckRequest(
        @Email(message = "올바른 이메일 주소 형식을 입력해주세요.") @Size(max = 255, message = "이메일은 최대 255자까지 가능합니다.") String email) {
}
