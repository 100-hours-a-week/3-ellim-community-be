package gguip1.community.domain.post.dto.request;

import gguip1.community.global.validation.ContentMax5000;
import gguip1.community.global.validation.TitleMax26;

import java.util.List;

public record PostUpdateRequest(
        @TitleMax26
        String title,
        @ContentMax5000
        String content,
        List<Long> imageIds
) {
}
