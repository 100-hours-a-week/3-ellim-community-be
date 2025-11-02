package gguip1.community.domain.post.controller;

import gguip1.community.domain.post.service.PostLikeService;
import gguip1.community.global.context.SecurityContext;
import gguip1.community.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostLikeController {
    private final PostLikeService postLikeService;

    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<ApiResponse<Void>> likePost(@PathVariable Long postId) {
        postLikeService.createLike(SecurityContext.getCurrentUserId(), postId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/posts/{postId}/like")
    public ResponseEntity<ApiResponse<Void>> unlikePost(@PathVariable Long postId) {
        postLikeService.deleteLike(SecurityContext.getCurrentUserId(), postId);
        return ResponseEntity.noContent().build();
    }
}
