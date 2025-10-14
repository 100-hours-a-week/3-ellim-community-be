package gguip1.community.domain.user.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DuplicateCheckResultTest {

    @Test
    void testHasAnyDuplicate_WhenBothExist() {
        DuplicateCheckResult result = new DuplicateCheckResult(true, true);
        assertTrue(result.hasAnyDuplicate());
        assertTrue(result.isEmailExists());
        assertTrue(result.isNicknameExists());
    }

    @Test
    void testHasAnyDuplicate_WhenOnlyEmailExists() {
        DuplicateCheckResult result = new DuplicateCheckResult(true, false);
        assertTrue(result.hasAnyDuplicate());
        assertTrue(result.isEmailExists());
        assertFalse(result.isNicknameExists());
    }

    @Test
    void testHasAnyDuplicate_WhenOnlyNicknameExists() {
        DuplicateCheckResult result = new DuplicateCheckResult(false, true);
        assertTrue(result.hasAnyDuplicate());
        assertFalse(result.isEmailExists());
        assertTrue(result.isNicknameExists());
    }

    @Test
    void testHasAnyDuplicate_WhenNoneExist() {
        DuplicateCheckResult result = new DuplicateCheckResult(false, false);
        assertFalse(result.hasAnyDuplicate());
        assertFalse(result.isEmailExists());
        assertFalse(result.isNicknameExists());
    }
}
