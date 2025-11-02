package gguip1.community.domain.user.mapper;

import gguip1.community.domain.auth.dto.AuthResponse;
import gguip1.community.domain.image.entity.Image;
import gguip1.community.domain.user.dto.request.UserCreateRequest;
import gguip1.community.domain.user.dto.response.UserResponse;
import gguip1.community.domain.user.dto.response.UserUpdateResponse;
import gguip1.community.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImage() != null ? user.getProfileImage().getUrl() : null)
                .nickname(user.getNickname())
                .build();
    }

    public User fromUserCreateRequest(UserCreateRequest request, String encryptedPassword, Image profileImage) {
        return User.builder()
                .profileImage(profileImage)
                .email(request.email())
                .password(encryptedPassword)
                .nickname(request.nickname())
                .build();
    }

    public AuthResponse toAuthResponse(User user) {
        return AuthResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImage() != null ? user.getProfileImage().getUrl() : null)
                .nickname(user.getNickname())
                .build();
    }

    public UserUpdateResponse toUserUpdateResponse(User user) {
        return new UserUpdateResponse(
                user.getUserId(),
                user.getEmail(),
                user.getProfileImage() != null ? user.getProfileImage().getUrl() : null,
                user.getNickname()
        );
    }
}
