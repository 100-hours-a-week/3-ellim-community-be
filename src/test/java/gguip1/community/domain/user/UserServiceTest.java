package gguip1.community.domain.user;

import gguip1.community.domain.user.dto.request.UserCreateRequest;
import gguip1.community.domain.user.entity.User;
import gguip1.community.domain.user.mapper.UserMapper;
import gguip1.community.domain.user.repository.UserRepository;
import gguip1.community.domain.user.service.UserService;
import gguip1.community.global.exception.ErrorCode;
import gguip1.community.global.exception.ErrorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void signupSuccessWithoutImage(){
        /*
         * 현재 createUser의 Response가 없기 때문에 검증하기 곤란함.
         * 저장(save) 호출만 검증함.
         */

        // given
        UserCreateRequest userCreateRequest = new UserCreateRequest("test@test.com", "test1234", "test1234", "test1", null);

        User mockUser = User.builder()
                .email("test@test.com")
                .nickname("test1")
                .password("encodedPassword")
                .build();

        given(passwordEncoder.encode("test1234")).willReturn("encodedPassword"); // 암호화 과정 흉내
        given(userMapper.fromUserCreateRequest(any(), anyString(), any()))
                .willReturn(User.builder()
                        .email("test@test.com")
                        .nickname("test1")
                        .password("encodedPassword")
                        .build()); // DTO -> Entity 변환 흉내

        given(userRepository.save(any(User.class))).willReturn(mockUser);

        // when
        userService.createUser(userCreateRequest);

        // then
        verify(userRepository).save(any(User.class));
    }

    @Test
    void signUpFailPasswordMismatch(){
        // given
        UserCreateRequest userCreateRequest = new UserCreateRequest("test@test.com", "test1234", "diff1234", "test1", null);

        // when
        ErrorException exception = assertThrows(ErrorException.class,
                () -> userService.createUser(userCreateRequest));

        // then
        assertEquals(ErrorCode.PASSWORD_MISMATCH, exception.getErrorCode());
    }
}
