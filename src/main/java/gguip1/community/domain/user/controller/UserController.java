package gguip1.community.domain.user.controller;

import gguip1.community.domain.user.dto.*;
import gguip1.community.domain.user.service.UserService;
import gguip1.community.global.response.ApiResponse;
import gguip1.community.global.security.CustomUserDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<Void>> createUser(@Valid @RequestBody UserCreateRequest request) {
        userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Registration successful", null));
    }

    @GetMapping("/users/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyInfo(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("get_user_success", userService.getUser(user.getUserId())));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("get_user_success", userService.getUser(userId)));
    }

    @PatchMapping("/users/me")
    public ResponseEntity<ApiResponse<Void>> updateMyInfo(@AuthenticationPrincipal CustomUserDetails user,
                                                          @Valid @RequestBody UserUpdateRequest requestBody) {
        userService.updateUser(user.getUserId(), requestBody);
        return ResponseEntity.noContent().build();
    }

    /*
     * 주석 처리의 근거는 다음과 같습니다.
     * - /users/me 로 본인 정보 수정이 가능함
     * - path variable로 userId를 받는 거 자체가 보안상 위험할 수 있다고 판단함
     * - 기존에는 관리자를 고려해서 만들었으나, 현재는 관리자 기능이 없음
     * - 추후 관리자 기능이 필요하게 된다면 현재 User Entity를 대대적으로 수저할 필요가 있음
     * - 따라서, 관리자 기능이 필요하게 될 때 다시 고려하는 것이 좋다고 판단됨
     */
//    @PatchMapping("/users/{userId}")
//    public ResponseEntity<ApiResponse<Void>> updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest request) {
//        userService.updateUser(userId, request);
//        return ResponseEntity.noContent().build();
//    }

    @PatchMapping("/users/me/password")
    public ResponseEntity<ApiResponse<Void>> updateMyPassword(@AuthenticationPrincipal CustomUserDetails user,
                                                              @Valid @RequestBody UserPasswordUpdateRequest requestBody,
                                                              HttpServletRequest httpRequest,
                                                              HttpServletResponse httpResponse) {
        userService.updateUserPassword(user.getUserId(), requestBody);

        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        httpResponse.addCookie(cookie);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/users/me")
    public ResponseEntity<Void> deleteMyAccount(@AuthenticationPrincipal CustomUserDetails user,
                                                HttpServletRequest httpRequest,
                                                HttpServletResponse httpResponse) {
        userService.deleteUser(user.getUserId());

        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        httpResponse.addCookie(cookie);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("users/check-email")
    public ResponseEntity<ApiResponse<UserEmailCheckResponse>> checkEmail(@Valid @RequestBody UserEmailCheckRequest userEmailCheckRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("email_available", new UserEmailCheckResponse(userService.existsByEmail(userEmailCheckRequest))));
    }

    @PostMapping("users/check-nickname")
    public ResponseEntity<ApiResponse<UserNicknameCheckResponse>> checkNickname(@Valid @RequestBody UserNicknameCheckRequest userNicknameCheckRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("nickname_available", new UserNicknameCheckResponse(userService.existsByNickname(userNicknameCheckRequest))));
    }
}
