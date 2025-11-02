package gguip1.community.domain.post.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostCommentPageItemResponse(
        Long commentId,
        String content,
        AuthorResponse author,
        Boolean isAuthor,
        LocalDateTime createdAt
) {
}
