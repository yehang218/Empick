import { routeMap } from '@/router/index.js';
import { RoleCode } from '@/constants/common/RoleCode.js';

export const fullMenu = {
    내정보: [
        {
            label: '기본 정보',
            roles: [RoleCode.USER, RoleCode.HR_ACCESS],
            path: '/orgstructure/profile',
            children: []
        },
        {
            label: '근태 기록',
            roles: [RoleCode.USER, RoleCode.HR_ACCESS],
            path: '/myinfo/attendance',
            children: []
        },
        {
            label: '일정 확인',
            roles: [RoleCode.USER, RoleCode.HR_ACCESS],
            path: '/myinfo/schedule',
            children: []
        }
    ],
    인사: [
        {
            label: '조직 관리',
            roles: [RoleCode.HR_ACCESS],
            children: [
                { label: '부서 관리', path: '/orgstructure/dept-manage' },
                { label: '직무 관리', path: '/orgstructure/job-manage' },
                { label: '직급 관리', path: '/orgstructure/rank-manage' },
                { label: '직책 관리', path: '/orgstructure/position-manage' }
            ]
        },
        {
            label: '사원 관리',
            roles: [RoleCode.HR_ACCESS],
            children: [
                { label: "신규 사원 등록", path: "/orgstructure/members/new" },
                { label: "사원 목록", path: "/orgstructure/members/list" },
                { label: "사원정보변경 요청", path: "/orgstructure/members/edit-request" }
            ]
        }
    ],
    근태: [
        {
            label: '전체 사원 근태 기록',
            roles: [RoleCode.HR_ACCESS],
            path: '/attendance/all',
            children: []
        }
    ],
    결재: [
        {
            label: '결재',
            roles: [RoleCode.USER, RoleCode.HR_ACCESS, RoleCode.APPROVAL_PROCESSOR],
            children: [
                {
                    label: '받은 결재',
                    path: routeMap.ApprovalInboxList,
                    roles: [RoleCode.HR_ACCESS, RoleCode.APPROVAL_PROCESSOR] // 인사팀, 팀장 전용
                },
                {
                    label: '결재 작성',
                    path: routeMap.ApprovalCreate,
                    roles: [RoleCode.USER, RoleCode.HR_ACCESS, RoleCode.APPROVAL_PROCESSOR]
                },
                {
                    label: '요청한 결재',
                    path: routeMap.ApprovalSentList,
                    roles: [RoleCode.USER, RoleCode.HR_ACCESS, RoleCode.APPROVAL_PROCESSOR]
                }
            ]
        }
    ],
    채용: [
        {
            label: '채용 공고',
            roles: [RoleCode.HR_ACCESS],
            children: [
                { label: '채용 공고', path: routeMap.RecruitmentList },
                { label: '채용 요청서', path: routeMap.RecruitmentRequestList },
//                 { label: '채용 공고 템플릿', path: '/employment/recruitments/templates' },
                { label: '자기소개서 템플릿', path: routeMap.IntroduceTemplateListPage }
            ]
        },
        {
            label: '실무테스트 관리',
            roles: [RoleCode.HR_ACCESS],
            children: [
                { label: '실무테스트 문제', path: routeMap.JobtestQuestionList },
                { label: '실무테스트', path: routeMap.JobtestList },
                { label: '실무테스트 답안', path: routeMap.JobtestAnswerList }
            ]
        },
        {
            label: '지원자',
            roles: [RoleCode.HR_ACCESS],
            children: [
                { label: '지원자 목록', path: routeMap.ApplicantPage }
            ]
        },
        {
            label: '면접 관리',
            roles: [RoleCode.HR_ACCESS],
            children: [
                { label: '면접 평가', path: '/employment/interviews' },
                { label: '면접 평가 기준표', path: '/employment/interview-criteria' }
            ]
        },
        {
            label: '안내 메일 관리',
            roles: [RoleCode.HR_ACCESS],
            children: [
                { label: '안내 메일', path: '/employment/emails' },
                { label: '안내 메일 템플릿', path: '/employment/email-templates' }
            ]
        }
    ],
    일정: [
        {
            label: '공식 일정',
            roles: [RoleCode.HR_ACCESS],
            children: []
        },
        {
            label: '부서일정',
            roles: [RoleCode.HR_ACCESS],
            children: []
        }
    ]
};
