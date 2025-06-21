export const stepTypeOptions = [
    { value: 'DOCUMENT', label: '서류전형' },
    { value: 'PRACTICAL', label: '실무테스트' },
    { value: 'INTERVIEW', label: '면접' }
]

export const getStepTypeLabel = (type) => {
    const map = {
        DOCUMENT: '서류전형',
        PRACTICAL: '실무테스트',
        INTERVIEW: '면접'
    }
    return map[type] || type
}
