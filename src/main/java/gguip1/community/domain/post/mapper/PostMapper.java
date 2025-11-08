package gguip1.community.domain.post.mapper;

import gguip1.community.domain.post.dto.request.PostCreateRequest;
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
}
