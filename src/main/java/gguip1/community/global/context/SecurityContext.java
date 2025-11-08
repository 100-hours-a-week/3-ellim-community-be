package gguip1.community.global.context;

/**
 * Thread Per Request 방식으로 현재 요청을 처리하는 사용자의 ID를 저장하고 조회하는 클래스입니다.
 * 사용 이유 :
 * Controller, Service, Repository 등 여러 계층에서 현재 사용자의 ID에 접근해야 할 때,
 * 매번 메서드 파라미터(HttpServletRequest)로 전달하는 것은 번거롭고 코드가 복잡해질 수 있습니다.
 * 이 클래스를 사용하면 ThreadLocal을 통해 현재 스레드에 사용자 ID를 저장하고,
 * 어디서든 쉽게 접근할 수 있어 편리합니다.
 * 주의 사항 :
 * ThreadLocal을 사용할 때는 반드시 요청 처리가 끝난 후에 clear() 메서드를 호출하여
 * 메모리 누수를 방지해야 합니다. 그렇지 않으면 스레드가 재사용될 때 이전 요청의 데이터가 남아 있을 수 있습니다.
 * 비동기 처리 환경에서는 ThreadLocal이 올바르게 동작하지 않을 수 있으므로 주의가 필요합니다.
 */
public class SecurityContext {
    private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();

    public static void setCurrentUserId(Long userId) {
        userIdHolder.set(userId);
    }

    public static Long getCurrentUserId() {
        return userIdHolder.get();
    }

    public static void clear() {
        userIdHolder.remove();
    }
}
