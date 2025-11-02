package gguip1.community.domain.post.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PostCommentPageResponse(
        List<PostCommentPageItemResponse> comments,
        boolean hasNext,
        Long lastCommentId
) {
}
