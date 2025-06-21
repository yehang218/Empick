/**
 * 권한이 맞는지 체크
 */
export function hasAccess(item, userRoles) {
    if (!item.roles) return true; // roles 없으면 모두 허용
    return item.roles.some(role => userRoles.includes(role));
}

/**
 * fullMenu 전체 구조를 사용자 권한에 맞게 "깊이까지" 필터링
 * (child/section 모두)
 * @param {object} fullMenu - 전체 메뉴 구조
 * @param {array} userRoles - 사용자 권한 배열
 * @returns {object} - 필터링된 메뉴 구조 (카테고리: section[] 형태)
 */
export function filterMenuByRoles(fullMenu, userRoles) {
    const filtered = {};
    for (const [category, sections] of Object.entries(fullMenu)) {
        // section의 roles도 체크!
        const filteredSections = sections
            .map(section => {
                // 1. section의 roles 체크 (없으면 모두 허용)
                if (section.roles && !section.roles.some(role => userRoles.includes(role))) return null;

                // 2. children도 roles별로 필터
                const filteredChildren = (section.children || []).filter(child =>
                    !child.roles || child.roles.some(role => userRoles.includes(role))
                );

                // 3. 자기자신이 path가 있으면, section도 체크
                if (
                    (filteredChildren.length > 0) ||
                    (section.path && (!section.roles || section.roles.some(role => userRoles.includes(role))))
                ) {
                    return { ...section, children: filteredChildren }
                }
                // 4. 아무것도 없으면 null
                return null
            })
            .filter(Boolean);

        // 5. 정말로 section이 1개라도 있어야 카테고리 추가
        if (filteredSections.length > 0) {
            filtered[category] = filteredSections;
        }
    }
    return filtered;
}
