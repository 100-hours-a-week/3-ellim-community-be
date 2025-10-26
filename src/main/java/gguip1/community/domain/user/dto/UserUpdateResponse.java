package gguip1.community.domain.user.dto;

public record UserUpdateResponse(Long userId, String email, String profileImageUrl, String nickname) {
}
