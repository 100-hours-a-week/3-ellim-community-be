package gguip1.community.domain.post.entity;

import gguip1.community.domain.user.entity.User;
import gguip1.community.global.entity.SoftDeleteEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends SoftDeleteEntity {
    @Id
    @Column(name = "post_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("imageOrder ASC")
    private List<PostImage> postImages = new ArrayList<>();

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private PostStat postStat;

    @Builder
    public Post(User user, String title, String content){
        this.user = user;
        this.title = title;
        this.content = content;
        this.postStat = new PostStat(this);
    }

    public void updatePost(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void addImage(PostImage postImage) {
        postImages.add(postImage);
    }
}
