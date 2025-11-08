package gguip1.community.global.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@ReportAsSingleViolation
@NotBlank
@Size(min = 1, max = 26)
public @interface TitleMax26 {
    String message() default "제목의 최대 길이는 26자입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
