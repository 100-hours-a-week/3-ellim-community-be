package gguip1.community.domain.auth.service;

import gguip1.community.domain.auth.dto.AuthRequest;
import gguip1.community.domain.auth.dto.AuthResponse;
import gguip1.community.domain.user.entity.User;
import gguip1.community.domain.user.mapper.UserMapper;
import gguip1.community.domain.user.repository.UserRepository;
import gguip1.community.global.exception.ErrorCode;
import gguip1.community.global.exception.ErrorException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(AuthRequest authRequest, HttpServletRequest httpRequest) {
        User user = userRepository.findByEmail(authRequest.email())
                .orElseThrow(() -> new ErrorException(ErrorCode.INVALID_CREDENTIALS));

        if (!checkPassword(authRequest.password(), user.getPassword())) {
            throw new ErrorException(ErrorCode.INVALID_CREDENTIALS);
        }

        return userMapper.toAuthResponse(user);
    }

    public void logout() {
    }

    private boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
