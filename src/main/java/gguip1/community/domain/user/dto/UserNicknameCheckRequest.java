package gguip1.community.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserNicknameCheckRequest(
        @NotBlank(message = "닉네임을 입력해주세요.") @Pattern(regexp = "^\\S+$", message = "띄어쓰기를 없애주세요.") @Size(max = 30, message = "닉네임은 최대 10자 까지 작성 가능합니다.") String nickname
) {
}
