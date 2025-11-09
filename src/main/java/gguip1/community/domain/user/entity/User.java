package gguip1.community.domain.user.entity;

import gguip1.community.domain.image.entity.Image;

import gguip1.community.global.entity.SoftDeleteEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends SoftDeleteEntity {
    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @OneToOne
    @JoinColumn(name = "profile_image_id")
    private Image profileImage;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Builder
    public User(Image profileImage, String email, String password, String nickname, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.profileImage = profileImage;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public void updateProfile(Image profileImage, String nickname){
        this.profileImage = profileImage;
        if (nickname != null){
            this.nickname = nickname;
        }
    }

    public void updatePassword(String password){
        this.password = password;
    }

    @Override
    public void softDelete(){
        this.status = 1;
        this.deletedAt = LocalDateTime.now();
        this.email += "_deleted";

//        this.nickname += "_deleted";
    }
}
