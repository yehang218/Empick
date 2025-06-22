/**
 * 실무테스트 문제 유형 매핑
 */

export const QUESTION_TYPE_MAP = {
    'MULTIPLE': '선택형',
    'SUBJECTIVE': '단답형',
    'DESCRIPTIVE': '서술형'
};

export const QUESTION_TYPES = {
    'MULTIPLE': '0',
    'SUBJECTIVE': '1',
    'DESCRIPTIVE': '2'
};

/**
 * 실무테스트 문제 유형 색상 매핑
 */
export const QUESTION_TYPE_COLORS = {
    'MULTIPLE': {
        background: '#81C784',
        text: '#2E7D32',
        hover: 'rgba(129, 199, 132, 0.3)'
    },
    'SUBJECTIVE': {
        background: '#FFB74D',
        text: '#E65100',
        hover: 'rgba(255, 183, 77, 0.3)'
    },
    'DESCRIPTIVE': {
        background: '#BA68C8',
        text: '#7B1FA2',
        hover: 'rgba(186, 104, 200, 0.3)'
    }
};

/**
 * 실무테스트 문제 유형 한글화
 * @param {keyof typeof QUESTION_TYPE_MAP} type - 문제 유형
 * @returns {string} 한글화된 문제 유형
 */
export const getQuestionTypeLabel = (type) => {
    return QUESTION_TYPE_MAP[type] || type;
};

/**
 * 실무테스트 문제 유형 색상 정보 반환
 * @param {keyof typeof QUESTION_TYPE_COLORS} type - 문제 유형
 * @returns {Object} 색상 정보 객체
 */
export const getQuestionTypeColors = (type) => {
    return QUESTION_TYPE_COLORS[type] || {
        background: '#90A4AE',
        text: '#37474F',
        hover: 'rgba(144, 164, 174, 0.3)'
    };
};

/**
 * 실무테스트 문제 유형 CSS 클래스명 반환
 * @param {keyof typeof QUESTION_TYPE_MAP} type - 문제 유형
 * @returns {string} CSS 클래스명
 */
export const getQuestionTypeClass = (type) => {
    switch (type) {
        case 'MULTIPLE':
        case '선택형':
            return 'type-multiple'
        case 'SUBJECTIVE':
        case '단답형':
            return 'type-subjective'
        case 'DESCRIPTIVE':
        case '서술형':
            return 'type-descriptive'
        default:
            return 'type-default'
    }
}; 