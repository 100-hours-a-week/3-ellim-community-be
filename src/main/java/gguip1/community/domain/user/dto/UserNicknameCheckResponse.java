package gguip1.community.domain.user.dto;

import lombok.Builder;

@Builder
public record UserNicknameCheckResponse(boolean isExisted) {

}
