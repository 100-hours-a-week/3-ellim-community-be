package gguip1.community.global.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@ReportAsSingleViolation
@NotBlank
@Email
@Size(min = 1, max = 247)
public @interface EmailMax247 {
    String message() default "이메일 형식이 올바르지 않거나, 최대 길이를 초과했습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
