package gguip1.community.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record PostCommentRequest(@NotBlank @Length(max = 300, message = "댓글의 최대 길이는 300자입니다.") String content) {
}
