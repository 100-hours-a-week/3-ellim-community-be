package gguip1.community.domain.post.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostCommentPageItemResponse(Long commentId, String content, AuthorDto author, Boolean isAuthor, LocalDateTime createdAt) {
}
