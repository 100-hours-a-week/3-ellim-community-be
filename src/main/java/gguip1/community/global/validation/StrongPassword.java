package gguip1.community.global.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@ReportAsSingleViolation
@NotBlank
@Size(min = 8, max = 20)
/*
 * 정규식 설명 : 비밀번호는 8자 이상 20자 이하이어야 하며,
 *            최소 하나의 소문자(?=.*[a-z]),
 *            최소 하나의 대문자(?=.*[A-Z]),
 *            최소 하나의 숫자(?=.*\d),
 *            최소 하나의 특수문자(?=.*[!@#$%^&*()_+\-={}[\]:";'<>?,./])를 포함해야 합니다.
 */
@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]:\";'<>?,./]).{8,20}$")
public @interface StrongPassword {
    String message() default "비밀번호 형식이 올바르지 않습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
