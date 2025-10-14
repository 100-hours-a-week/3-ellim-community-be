package gguip1.community.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 이메일 및 닉네임 중복 확인 결과 DTO
 */
@Getter
@AllArgsConstructor
public class DuplicateCheckResult {
    private boolean emailExists;
    private boolean nicknameExists;
    
    public boolean hasAnyDuplicate() {
        return emailExists || nicknameExists;
    }
}
