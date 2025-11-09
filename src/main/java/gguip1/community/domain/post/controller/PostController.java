package gguip1.community.domain.post.controller;

import gguip1.community.domain.post.dto.request.PostCreateRequest;
import gguip1.community.domain.post.dto.request.PostUpdateRequest;
import gguip1.community.domain.post.dto.response.PostDetailResponse;
import gguip1.community.domain.post.dto.response.PostPageItemResponse;
import gguip1.community.domain.post.dto.response.PostPageResponse;
import gguip1.community.domain.post.service.PostService;
import gguip1.community.global.context.SecurityContext;
import gguip1.community.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<PostPageItemResponse>> createPost(@Valid @RequestBody PostCreateRequest postCreateRequest) {
        PostPageItemResponse response = postService.createPost(SecurityContext.getCurrentUserId(), postCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("Post created", response)
        );
    }

    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<PostPageResponse>> getPosts(
            @RequestParam Optional<Long> lastPostId
    ){
        PostPageResponse response = postService.getPosts(lastPostId.orElse(null), 5);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success("Posts retrieved successfully", response)
        );
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<PostDetailResponse>> getPost(@PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success("Posts retrieved successfully", postService.getPostDetail(SecurityContext.getCurrentUserId(), postId))
        );
    }

    @PatchMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<Void>> updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequest postUpdateRequest) {
        postService.updatePost(SecurityContext.getCurrentUserId(), postId, postUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success("Post updated", null));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long postId) {
        postService.deletePost(SecurityContext.getCurrentUserId(), postId);
        return ResponseEntity.noContent().build();
    }
}
