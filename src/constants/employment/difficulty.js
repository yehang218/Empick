/**
 * 실무테스트 난이도 매핑
 */
export const DIFFICULTY_MAP = {
    'EASY': '쉬움',
    'MEDIUM': '보통',
    'HARD': '어려움'
};

export const DIFFICULTY_TYPES = {
    'EASY': '0',
    'MEDIUM': '1',
    'HARD': '2'
};

/**
 * 실무테스트 난이도 색상 매핑
 */
export const DIFFICULTY_COLORS = {
    'EASY': {
        background: '#e8f5e9',
        text: '#2e7d32',
        hover: 'rgba(232, 245, 233, 0.4)'
    },
    'MEDIUM': {
        background: '#e8eaf6',
        text: '#3f51b5',
        hover: 'rgba(232, 234, 246, 0.4)'
    },
    'HARD': {
        background: '#ffcdd2',
        text: '#c62828',
        hover: 'rgba(255, 205, 210, 0.4)'
    },
    '쉬움': {
        background: '#e8f5e9',
        text: '#2e7d32',
        hover: 'rgba(232, 245, 233, 0.4)'
    },
    '보통': {
        background: '#e8eaf6',
        text: '#3f51b5',
        hover: 'rgba(232, 234, 246, 0.4)'
    },
    '어려움': {
        background: '#ffcdd2',
        text: '#c62828',
        hover: 'rgba(255, 205, 210, 0.4)'
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