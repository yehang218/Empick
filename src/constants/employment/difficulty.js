/**
 * 실무테스트 난이도 매핑
 */
export const DIFFICULTY_MAP = {
    'EASY': '쉬움',
    'MEDIUM': '보통',
    'HARD': '어려움'
};

/**
 * 실무테스트 난이도 한글화
 * @param {keyof typeof DIFFICULTY_MAP} difficulty - 난이도
 * @returns {string} 한글화된 난이도
 */
export const getDifficultyLabel = (difficulty) => {
    return DIFFICULTY_MAP[difficulty] || difficulty;
}; 