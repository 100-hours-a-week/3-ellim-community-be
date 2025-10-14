package gguip1.community.domain.user.service;

import gguip1.community.domain.auth.entity.Session;
import gguip1.community.domain.image.entity.Image;
import gguip1.community.domain.image.repository.ImageRepository;
import gguip1.community.domain.user.dto.UserPasswordUpdateRequest;
import gguip1.community.domain.user.dto.UserRequest;
import gguip1.community.domain.user.dto.UserResponse;
import gguip1.community.domain.user.dto.UserUpdateRequest;
import gguip1.community.domain.user.entity.User;
import gguip1.community.domain.user.repository.UserRepository;
import gguip1.community.global.exception.ErrorCode;
import gguip1.community.global.exception.ErrorException;
import gguip1.community.global.session.SessionManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public void createUser(UserRequest request) {
        if (!request.getPassword().equals(request.getPassword2())){
            throw new ErrorException(ErrorCode.PASSWORD_MISMATCH);
        }

        var duplicateCheck = userRepository.checkDuplicates(request.getEmail(), request.getNickname());
        
        if (duplicateCheck.isEmailExists()) {
            throw new ErrorException(ErrorCode.DUPLICATE_EMAIL);
        }
        
        if (duplicateCheck.isNicknameExists()) {
            throw new ErrorException(ErrorCode.DUPLICATE_NICKNAME);
        }

        Image profileImage = null;
        if (request.getProfileImageId() != null) {
            profileImage = imageRepository.findById(request.getProfileImageId())
                    .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND));
        }

        User user = User.builder()
                .profileImage(profileImage)
                .email(request.getEmail())
                .password(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()))
                .nickname(request.getNickname())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }

    public UserResponse getMyInfo(Session session) {
        return getUser(session.getUserId());
    }

    public UserResponse getUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));

        return new UserResponse(
                user.getEmail(),
                user.getNickname(),
                user.getProfileImage() != null ? user.getProfileImage().getUrl() : null
        );
    }

    public void updateMyInfo(Session session, UserUpdateRequest request) {
        updateUser(session.getUserId(), request);
    }

    @Transactional
    public void updateUser(Integer userId, UserUpdateRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));

        if (request.getNickname() != null && !request.getNickname().equals(user.getNickname())) {
            boolean exists = userRepository.existsByNickname(request.getNickname());
            if (exists) {
                throw new ErrorException(ErrorCode.DUPLICATE_NICKNAME);
            }
            user.changeNickname(request.getNickname());
        }

        if (request.getProfileImageId() != null){
            Image profileImage = imageRepository.findById(request.getProfileImageId())
                    .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND));
            user.changeProfileImage(profileImage);
        }

            userRepository.save(user);
    }

    public void updateMyPassword(Session session, UserPasswordUpdateRequest request) {
        updateUserPassword(session.getUserId(), request);
    }

    @Transactional
    public void updateUserPassword(Integer userId, UserPasswordUpdateRequest request){
        if (!request.getNewPassword().equals(request.getNewPassword2())){
            throw new ErrorException(ErrorCode.PASSWORD_MISMATCH);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));

        user.changePassword(BCrypt.hashpw(request.getNewPassword(), BCrypt.gensalt()));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ErrorException(ErrorCode.NOT_FOUND);
        }

        userRepository.deleteById(userId);
    }
}
