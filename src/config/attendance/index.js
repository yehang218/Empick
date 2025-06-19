/**
 * 근태 설정 통합 export
 * 모든 근태 관련 설정을 한 곳에서 import할 수 있도록 제공
 */

export { WORK_TIME_CONFIG } from './workTimeConfig.js'
export { BUSINESS_RULES } from './businessRules.js'
export { ATTENDANCE_CONSTANTS } from './constants.js'

// 개별 import 후 통합 객체 생성
import { WORK_TIME_CONFIG } from './workTimeConfig.js'
import { BUSINESS_RULES } from './businessRules.js'
import { ATTENDANCE_CONSTANTS } from './constants.js'

// 편의를 위한 통합 객체도 제공
export const ATTENDANCE_CONFIG = {
    WORK_TIME_CONFIG,
    BUSINESS_RULES,
    ATTENDANCE_CONSTANTS
} 