package gguip1.community.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 400
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST,  "VALIDATION_ERROR", "요청 데이터가 올바르지 않습니다."),

    // 401
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "인증이 필요합니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "인증 정보가 올바르지 않습니다."),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "PASSWORD_MISMATCH","비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "USER_NOT_FOUND","사용자를 찾을 수 없습니다."),
    SESSION_EXPIRED(HttpStatus.UNAUTHORIZED, "SESSION_EXPIRED", "세션이 만료되었습니다. 다시 로그인해주세요."),

    // 403
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "ACCESS_DENIED","접근이 권한이 없습니다."),

    // 404
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "요청하신 리소스를 찾을 수 없습니다."),

    // 409
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "DUPLICATE_EMAIL", "이미 사용 중인 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "DUPLICATE_NICKNAME", "이미 사용 중인 닉네임입니다."),
    DUPLICATE_LIKE(HttpStatus.CONFLICT, "DUPLICATE_LIKE", "이미 좋아요를 눌렀습니다."),

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버에 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
