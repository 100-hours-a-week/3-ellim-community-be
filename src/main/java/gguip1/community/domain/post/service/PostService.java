package gguip1.community.domain.post.service;

import gguip1.community.domain.auth.entity.Session;
import gguip1.community.domain.image.entity.Image;
import gguip1.community.domain.image.repository.ImageRepository;
import gguip1.community.domain.post.dto.*;
import gguip1.community.domain.post.entity.*;
import gguip1.community.domain.post.id.PostImageId;
import gguip1.community.domain.post.id.PostLikeId;
import gguip1.community.domain.post.mapper.PostImageMapper;
import gguip1.community.domain.post.mapper.PostLikeMapper;
import gguip1.community.domain.post.mapper.PostMapper;
import gguip1.community.domain.post.repository.PostCommentRepository;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class PostService {
    // Repository
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final PostImageRepository postImageRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostCommentRepository postCommentRepository;

    // Post <-> Post 관련 DTO 상호 변환용 Mapper
    private final PostMapper postMapper;

    // PostImage 상호 변환용 Mapper
    private final PostImageMapper postImageMapper;

    // PostLike 상호 변환용 Mapper
    private final PostLikeMapper postLikeMapper;

    @Transactional
    public void createPost(Long userId, PostRequest postRequest) {
        List<Image> images = Collections.emptyList();
        if (postRequest.imageIds() != null && !postRequest.imageIds().isEmpty()) {
            images = imageRepository.findAllById(postRequest.imageIds());
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));

        Post post = postMapper.fromPostRequest(postRequest, user);

        postRepository.save(post);

        AtomicInteger imageOrder = new AtomicInteger(0);
        List<PostImage> postImages = images.stream()
                .map(image -> postImageMapper.toEntity(post, image, (byte) imageOrder.getAndIncrement()))
                .toList();

        postImageRepository.saveAll(postImages);
    }

    @Transactional
    public void createLike(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND));

        if (postLikeRepository.existsById(new PostLikeId(userId, postId))) {
            throw new ErrorException(ErrorCode.DUPLICATE_LIKE);
        }

        PostLikeId postLikeId = new PostLikeId(userId, postId);
        PostLike postLike = postLikeMapper.toEntity(postLikeId, post, user);

        post.getPostStat().incrementLikeCount();
        postLikeRepository.save(postLike);

        /**
         * 개선 필요한 부분 JPQL로 엔티티에서 좋아요 증가가 아닌 DB 레벨에서 증가하도록 수정 필요
         */
        postRepository.save(post);
    }

    @Transactional
    public void deleteLike(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND));

        PostLikeId postLikeId = new PostLikeId(userId, postId);

        if (!postLikeRepository.existsById(postLikeId)) {
            throw new ErrorException(ErrorCode.NOT_FOUND);
        }

        /**
         * 개선 필요한 부분 JPQL로 엔티티에서 좋아요 증가가 아닌 DB 레벨에서 증가하도록 수정 필요
         */
        post.getPostStat().decrementLikeCount();
        postLikeRepository.deleteById(postLikeId);
        postRepository.save(post);
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
                            .author(new AuthorDto(
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
                .author(new AuthorDto(
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

        postRepository.delete(post);
    }
}
