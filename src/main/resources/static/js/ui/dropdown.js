export function initDropdown(buttonSelector, dropdownSelector) {
    const profileBtn = document.querySelector(buttonSelector);
    const profileDropdown = document.querySelector(dropdownSelector);

    if (!profileBtn || !profileDropdown) return;

    // 드롭다운 토글
    profileBtn.addEventListener('click', (e) => {
        e.stopPropagation();
        const isOpen = profileDropdown.classList.contains('show');
        
        if (isOpen) {
            closeDropdown();
        } else {
            openDropdown();
        }
    });

    // 외부 클릭 시 드롭다운 닫기
    document.addEventListener('click', (e) => {
        if (!profileDropdown.contains(e.target) && !profileBtn.contains(e.target)) {
            closeDropdown();
        }
    });

    // ESC 키로 드롭다운 닫기
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && profileDropdown.classList.contains('show')) {
            closeDropdown();
            profileBtn.focus();
        }
    });

    // 드롭다운 내부 키보드 네비게이션
    profileDropdown.addEventListener('keydown', (e) => {
        const menuItems = profileDropdown.querySelectorAll('[role="menuitem"]');
        const currentIndex = Array.from(menuItems).indexOf(e.target);
        
        switch (e.key) {
            case 'ArrowDown':
                e.preventDefault();
                const nextIndex = (currentIndex + 1) % menuItems.length;
                menuItems[nextIndex].focus();
                break;
            case 'ArrowUp':
                e.preventDefault();
                const prevIndex = currentIndex <= 0 ? menuItems.length - 1 : currentIndex - 1;
                menuItems[prevIndex].focus();
                break;
            case 'Home':
                e.preventDefault();
                menuItems[0].focus();
                break;
            case 'End':
                e.preventDefault();
                menuItems[menuItems.length - 1].focus();
                break;
        }
    });

    function openDropdown() {
        profileDropdown.classList.add('show');
        profileBtn.setAttribute('aria-expanded', 'true');
        profileDropdown.setAttribute('aria-hidden', 'false');
        
        // 첫 번째 메뉴 아이템에 포커스
        const firstMenuItem = profileDropdown.querySelector('[role="menuitem"]');
        if (firstMenuItem) {
            firstMenuItem.focus();
        }
    }

    function closeDropdown() {
        profileDropdown.classList.remove('show');
        profileBtn.setAttribute('aria-expanded', 'false');
        profileDropdown.setAttribute('aria-hidden', 'true');
    }
}
