package gguip1.community.domain.post.service;

import gguip1.community.domain.image.entity.Image;
import gguip1.community.domain.image.repository.ImageRepository;
import gguip1.community.domain.post.dto.request.PostCreateRequest;
import gguip1.community.domain.post.dto.request.PostUpdateRequest;
import gguip1.community.domain.post.dto.response.AuthorResponse;
import gguip1.community.domain.post.dto.response.PostDetailResponse;
import gguip1.community.domain.post.dto.response.PostPageItemResponse;
import gguip1.community.domain.post.dto.response.PostPageResponse;
import gguip1.community.domain.post.entity.*;
import gguip1.community.domain.post.id.PostImageId;
import gguip1.community.domain.post.id.PostLikeId;
import gguip1.community.domain.post.mapper.PostImageMapper;
import gguip1.community.domain.post.mapper.PostMapper;
import gguip1.community.domain.post.repository.PostImageRepository;
import gguip1.community.domain.post.repository.PostLikeRepository;
import gguip1.community.domain.post.repository.PostRepository;
import gguip1.community.domain.user.entity.User;
import gguip1.community.domain.user.repository.UserRepository;
import gguip1.community.global.exception.ErrorCode;
import gguip1.community.global.exception.ErrorException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final PostImageRepository postImageRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    private final PostMapper postMapper;

    private final PostImageMapper postImageMapper;

    @Transactional
    public void createPost(Long userId, PostCreateRequest postCreateRequest) {
        List<Image> images = Collections.emptyList();
        if (postCreateRequest.imageIds() != null && !postCreateRequest.imageIds().isEmpty()) {
            images = imageRepository.findAllById(postCreateRequest.imageIds());
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));

        Post post = postMapper.fromPostRequest(postCreateRequest, user);

        postRepository.save(post);

        AtomicInteger imageOrder = new AtomicInteger(0);
        List<PostImage> postImages = images.stream()
                .map(image -> postImageMapper.toEntity(post, image, (byte) imageOrder.getAndIncrement()))
                .toList();

        postImageRepository.saveAll(postImages);
    }

    @Transactional
    public PostPageResponse getPosts(Long lastPostId, int pageSize) {
        List<Post> posts =
                lastPostId == null ? postRepository.findFirstPage(pageSize + 1) : postRepository.findNextPage(lastPostId, pageSize + 1);

        boolean hasNext = posts.size() > pageSize;

        List<PostPageItemResponse> postPageItemResponses = posts.stream()
                .limit(pageSize)
                .map(post -> {
                    User user = post.getUser();

                    List<String> imageUrls = post.getPostImages().stream()
                            .map(postImage -> postImage.getImage().getUrl())
                            .toList();

                    return PostPageItemResponse.builder()
                            .postId(post.getPostId())
                            .imageUrls(imageUrls)
                            .title(post.getTitle())
                            .content(post.getContent())
                            .author(new AuthorResponse(
                                    user.getNickname(),
                                    user.getProfileImage() != null ? user.getProfileImage().getUrl() : null
                            ))
                            .createdAt(post.getCreatedAt())
                            .likeCount(post.getPostStat().getLikeCount())
                            .commentCount(post.getPostStat().getCommentCount())
                            .viewCount(post.getPostStat().getViewCount())
                            .build();
                })
                .toList();

        Long newLastPostId = postPageItemResponses.isEmpty() ? null :
                postPageItemResponses.getLast().postId();

        return new PostPageResponse(
                postPageItemResponses,
                hasNext,
                newLastPostId
        );
    }

    @Transactional
    public PostDetailResponse getPostDetail(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND));

        post.getPostStat().incrementViewCount();
        postRepository.save(post);

        User user = post.getUser();

        List<String> imageUrls = post.getPostImages().stream()
                .map(postImage -> postImage.getImage().getUrl())
                .toList();

        boolean isAuthor = userId != null && userId.equals(user.getUserId());
        boolean isLiked = userId != null && postLikeRepository.existsById(new PostLikeId(userId, postId));

        return PostDetailResponse.builder()
                .imageUrls(imageUrls)
                .title(post.getTitle())
                .content(post.getContent())
                .author(new AuthorResponse(
                        user.getNickname(),
                        user.getProfileImage() != null ? user.getProfileImage().getUrl() : null
                ))
                .createdAt(post.getCreatedAt())
                .likeCount(post.getPostStat().getLikeCount())
                .commentCount(post.getPostStat().getCommentCount())
                .viewCount(post.getPostStat().getViewCount())
                .isAuthor(isAuthor)
                .isLiked(isLiked)
                .build();
    }


    @Transactional
    public void updatePost(Long userId, Long postId, PostUpdateRequest postUpdateRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND));

        if (!post.getUser().getUserId().equals(userId)) {
            throw new ErrorException(ErrorCode.ACCESS_DENIED);
        }

        post.updatePost(postUpdateRequest.title(), postUpdateRequest.content());

        postRepository.save(post);

        postImageRepository.deleteAllByPost_PostId(postId);

        List<Image> images = Collections.emptyList();
        if (postUpdateRequest.imageIds() != null && !postUpdateRequest.imageIds().isEmpty()) {
            images = imageRepository.findAllById(postUpdateRequest.imageIds());
        }

        AtomicInteger order = new AtomicInteger(0);
        List<PostImage> postImages = images.stream()
                .map(image -> {
                    PostImageId postImageId = new PostImageId(post.getPostId(), image.getImageId());
                    return PostImage.builder()
                            .postImageId(postImageId)
                            .post(post)
                            .image(image)
                            .imageOrder((byte) order.getAndIncrement())
                            .build();
                })
                .toList();
        postImageRepository.saveAll(postImages);
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND));

        if (!post.getUser().getUserId().equals(userId)) {
            throw new ErrorException(ErrorCode.ACCESS_DENIED);
        }

        post.softDelete();
        postRepository.save(post);
    }
}
