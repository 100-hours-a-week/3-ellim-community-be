package gguip1.community.domain.user.dto.response;

import lombok.Builder;

@Builder
public record UserNicknameCheckResponse(
        boolean isExisted
) {
}
