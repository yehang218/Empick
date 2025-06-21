// 1. 문자열을 그대로 사용
export const RECRUIT_TYPE_ENUM = {
    REGULAR: 'REGULAR',
    SEASONAL: 'SEASONAL'
};

// 2. 문자열 기반으로 한글 label 매핑
export const RECRUIT_TYPE_MAP = {
    REGULAR: '상시',
    SEASONAL: '공채'
};

// 3. 문자열 enum 값 받아 label로 바꿔주는 함수
export const getRecruitTypeLabel = (code) => {
    return RECRUIT_TYPE_MAP[code] || code;
};

// 4. 셀렉트 옵션용
export const recruitTypeOptions = [
    { value: 'REGULAR', label: '상시' },
    { value: 'SEASONAL', label: '공채' }
];