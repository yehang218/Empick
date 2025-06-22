/**
 * 실무테스트 난이도 매핑
 */
export const DIFFICULTY_MAP = {
    'EASY': '쉬움',
    'MEDIUM': '보통',
    'HARD': '어려움'
};

/**
 * 실무테스트 난이도 색상 매핑
 */
export const DIFFICULTY_COLORS = {
    'EASY': {
        background: '#A5D6A7',
        text: '#2E7D32',
        hover: 'rgba(165, 214, 167, 0.3)'
    },
    'MEDIUM': {
        background: '#90CAF9',
        text: '#1565C0',
        hover: 'rgba(144, 202, 249, 0.3)'
    },
    'HARD': {
        background: '#EF9A9A',
        text: '#C62828',
        hover: 'rgba(239, 154, 154, 0.3)'
    }
};

/**
 * 실무테스트 난이도 한글화
 * @param {keyof typeof DIFFICULTY_MAP} difficulty - 난이도
 * @returns {string} 한글화된 난이도
 */
export const getDifficultyLabel = (difficulty) => {
    return DIFFICULTY_MAP[difficulty] || difficulty;
};

/**
 * 실무테스트 난이도 색상 정보 반환
 * @param {keyof typeof DIFFICULTY_COLORS} difficulty - 난이도
 * @returns {Object} 색상 정보 객체
 */
export const getDifficultyColors = (difficulty) => {
    return DIFFICULTY_COLORS[difficulty] || DIFFICULTY_COLORS.MEDIUM;
};

/**
 * 실무테스트 난이도 CSS 클래스명 반환
 * @param {keyof typeof DIFFICULTY_MAP} difficulty - 난이도
 * @returns {string} CSS 클래스명
 */
export const getDifficultyClass = (difficulty) => {
    switch (difficulty) {
        case 'EASY':
        case '쉬움':
            return 'difficulty-easy'
        case 'MEDIUM':
        case '보통':
            return 'difficulty-medium'
        case 'HARD':
        case '어려움':
            return 'difficulty-hard'
        default:
            return 'difficulty-medium'
    }
}; 