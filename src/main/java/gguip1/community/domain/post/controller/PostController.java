package gguip1.community.domain.post.controller;

import gguip1.community.domain.auth.entity.Session;
import gguip1.community.domain.post.dto.*;
import gguip1.community.domain.post.service.PostService;
import gguip1.community.global.response.ApiResponse;
import gguip1.community.global.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<Void>> createPost(@AuthenticationPrincipal CustomUserDetails user, @Valid @RequestBody PostRequest postRequest) {
        postService.createPost(user.getUserId(), postRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("Post created", null)
        );
    }

    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<PostPageResponse>> getPosts(
            @RequestParam Optional<Long> lastPostId
    ){
        PostPageResponse response = postService.getPosts(lastPostId.orElse(null), 5);
        System.out.println(response);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success("Posts retrieved successfully", response)
        );
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<PostDetailResponse>> getPost(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success("Posts retrieved successfully", postService.getPostDetail(user.getUserId(), postId))
        );
    }

    @PatchMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<Void>> updatePost(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long postId, @RequestBody PostUpdateRequest postUpdateRequest) {
        postService.updatePost(user.getUserId(), postId, postUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success("Post updated", null));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long postId) {
        postService.deletePost(user.getUserId(), postId);
        return ResponseEntity.noContent().build();
    }
}
