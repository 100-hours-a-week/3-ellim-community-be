package gguip1.community.domain.user.repository;

import gguip1.community.domain.user.dto.DuplicateCheckResult;
import gguip1.community.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    
    /**
     * 이메일과 닉네임의 중복 여부를 한 번의 쿼리로 확인합니다.
     * 
     * @param email 확인할 이메일
     * @param nickname 확인할 닉네임
     * @return 중복 확인 결과 (이메일 중복 여부, 닉네임 중복 여부)
     */
    @Query("SELECT new gguip1.community.domain.user.dto.DuplicateCheckResult(" +
           "CASE WHEN COUNT(CASE WHEN u.email = :email THEN 1 END) > 0 THEN true ELSE false END, " +
           "CASE WHEN COUNT(CASE WHEN u.nickname = :nickname THEN 1 END) > 0 THEN true ELSE false END" +
           ") FROM User u WHERE u.email = :email OR u.nickname = :nickname")
    DuplicateCheckResult checkDuplicates(@Param("email") String email, @Param("nickname") String nickname);
}
