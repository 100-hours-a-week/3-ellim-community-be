package gguip1.community.domain.post.dto.request;

import gguip1.community.global.validation.CommentMax300;

public record PostCommentRequest(
        @CommentMax300
        String content
) {
}
