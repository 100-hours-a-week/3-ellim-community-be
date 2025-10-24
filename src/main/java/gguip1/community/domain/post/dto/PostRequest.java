package gguip1.community.domain.post.dto;

import org.hibernate.validator.constraints.Length;

import java.util.List;

public record PostRequest(@Length(max = 26, message = "게시글 제목의 최대 길이는 26자입니다.") String title, @Length(max = 5000, message = "게시글의 최대 길이는 5000자입니다.") String content, List<Long> imageIds) {
}
