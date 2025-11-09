package gguip1.community.domain.post.repository;

import gguip1.community.domain.post.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    @Query(value = """
        SELECT * FROM post_comments
        WHERE post_id = :postId
        ORDER BY comment_id ASC
        LIMIT :limit""", nativeQuery = true)
    List<PostComment> findFirstPageByPostId(@Param("postId") Long postId,@Param("limit") int limit);

    @Query(value = """
        SELECT * FROM post_comments
        WHERE post_id = :postId AND (:lastCommentId IS NULL OR comment_id > :lastCommentId)
        ORDER BY comment_id ASC
        LIMIT :limit""", nativeQuery = true)
    List<PostComment> findNextPageByPostId(@Param("postId") Long postId, @Param("lastCommentId") Long lastCommentId, @Param("limit") int limit);

    @Query(value = """
            SELECT * FROM post_comments
            WHERE post_id = :postId
            ORDER BY comment_id DESC
            LIMIT :limit
            """, nativeQuery = true
    )
    List<PostComment> findLastestPageByPostId(@Param("postId") Long postId, @Param("limit") int limit);

    @Query(value = """
            SELECT * FROM post_comments
            WHERE post_id = :postId AND comment_id < :firstCommentId
            ORDER BY comment_id DESC
            LIMIT :limit
            """, nativeQuery = true
    )
    List<PostComment> findPrevPageByPostId(@Param("postId") Long postId, @Param("firstCommentId") Long firstCommentId, @Param("limit") int limit);
}
