package gguip1.community.domain.user.service;

import gguip1.community.domain.image.entity.Image;
import gguip1.community.domain.image.repository.ImageRepository;
import gguip1.community.domain.user.dto.request.*;
import gguip1.community.domain.user.dto.response.UserResponse;
import gguip1.community.domain.user.dto.response.UserUpdateResponse;
import gguip1.community.domain.user.entity.User;
import gguip1.community.domain.user.mapper.UserMapper;
import gguip1.community.domain.user.repository.UserRepository;
import gguip1.community.global.exception.ErrorCode;
import gguip1.community.global.exception.ErrorException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

//    @Transactional
    public void createUser(UserCreateRequest request) {
        if (!request.password().equals(request.password2())){
            throw new ErrorException(ErrorCode.PASSWORD_MISMATCH);
        } // 비밀번호 불일치 확인

        String encodedPassword = passwordEncoder.encode(request.password()); // 비밀번호 암호화

        if (userRepository.existsByEmail(request.email())) {
            throw new ErrorException(ErrorCode.DUPLICATE_EMAIL);
        } // 이메일 중복 확인

        if (userRepository.existsByNickname(request.nickname())) {
            throw new ErrorException(ErrorCode.DUPLICATE_NICKNAME);
        } // 닉네임 중복 확인

        Image profileImage = null;
        if (request.profileImageId() != null){
            profileImage = imageRepository.findById(request.profileImageId())
                    .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND));
        } // 프로필 이미지 설정

        userRepository.save(userMapper.fromUserCreateRequest(request, encodedPassword, profileImage)); // DB에 저장
    }

    // users/{userId} (관리자 등 타인) 정보 조회
    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toResponse(user);
    }

    // users/{userId} (관리자 등 타인) 정보 수정
    @Transactional
    public UserUpdateResponse updateUser(Long userId, UserUpdateRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));

        Image profileImage = null;
        if (request.profileImageId() != null){
            profileImage = imageRepository.findById(request.profileImageId())
                    .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND));
        }

        if (!request.nickname().equals(user.getNickname())) {
            if (userRepository.existsByNickname(request.nickname())) {
                throw new ErrorException(ErrorCode.DUPLICATE_NICKNAME);
            }
        }

        user.updateProfile(profileImage, request.nickname());

        userRepository.save(user);

        return userMapper.toUserUpdateResponse(user);
    }

    @Transactional
    public void updateUserPassword(Long userId, UserPasswordUpdateRequest request){
        if (!request.newPassword().equals(request.newPassword2())){
            throw new ErrorException(ErrorCode.PASSWORD_MISMATCH);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));

        String encodedPassword = passwordEncoder.encode(request.newPassword());
        user.updatePassword(encodedPassword);

        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));

        user.softDelete();

        userRepository.save(user);
    }

    public boolean existsByEmail(UserEmailCheckRequest request) {
        return userRepository.existsByEmail(request.email());
    }

    public boolean existsByNickname(UserNicknameCheckRequest request) {
        return userRepository.existsByNickname(request.nickname());
    }
}
