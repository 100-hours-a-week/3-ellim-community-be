package gguip1.community.domain.post.mapper;

import gguip1.community.domain.post.dto.request.PostCreateRequest;
import gguip1.community.domain.post.dto.response.AuthorResponse;
import gguip1.community.domain.post.dto.response.PostPageItemResponse;
import gguip1.community.domain.post.entity.Post;
import gguip1.community.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    public Post fromPostRequest(PostCreateRequest postCreateRequest, User user){
        return Post.builder()
                .user(user)
                .title(postCreateRequest.title())
                .content(postCreateRequest.content())
                .build();
    }

    public PostPageItemResponse toPostPageItemResponse(Post post, User user, Integer likeCount, Integer commentCount, Integer viewCount){
        return PostPageItemResponse.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(
                        new AuthorResponse(
                                user.getNickname(),
                                user.getProfileImage() != null ? user.getProfileImage().getUrl() : null
                        )
                )
                .createdAt(post.getCreatedAt())
                .likeCount(likeCount)
                .commentCount(commentCount)
                .viewCount(viewCount)
                .build();
    }
}
