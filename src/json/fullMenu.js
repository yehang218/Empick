import { routeMap } from '@/router/index.js';

// 메뉴 구조
export const fullMenu = {
    내정보: [
        {
            label: "기본 정보",
            role: ["사원", "인사팀"],
            path: "/myinfo/profile",
            children: []
        },
        {
            label: "근태 기록",
            role: ["사원", "인사팀"],
            path: "/myinfo/attendance",
            children: []
        },
        {
            label: "일정 확인",
            role: ["사원", "인사팀"],
            path: "/myinfo/schedule",
            children: []
        },
    ],
    인사: [
        {
            label: "조직 관리",
            role: ["인사팀"],
            children: [
                { label: "부서 관리", path: "/hr/department" },
                { label: "직무 관리", path: "/hr/position" },
                { label: "직급 관리", path: "/hr/level" },
                { label: "직책 관리", path: "/hr/title" }
            ]
        },
        {
            label: "사원 관리",
            role: ["인사팀"],
            children: [
                { label: "신규 사원 등록", path: "/hr/employees/new" },
                { label: "사원 목록", path: "/hr/employees/list" },
                { label: "사원정보변경 요청", path: "/hr/employees/edit-request" }
            ]
        }
    ],
    근태: [
        {
            label: "전체 사원 근태 기록",
            role: ["인사팀"],
            path: "/attendance/all",
            children: []
        }
    ],
    채용: [
        {
            label: "채용 공고",
            role: ["인사팀"],
            children: [
                { label: "채용 공고", path: routeMap.RecruitmentList },
                { label: "채용 요청서", path: routeMap.RecruitmentRequestList },
                { label: "채용 공고 템플릿", path: "/employment/recruitments/templates" },
                { label: "자기소개서 템플릿", path: "/employment/recruitments/introduce-templates" }
            ]
        },
        {
            label: "실무테스트 관리",
            role: ["인사팀"],
            children: [
                { label: "실무테스트 문제", path: routeMap.JobtestQuestionList },
                { label: "실무테스트", path: routeMap.JobtestList },
                { label: "실무테스트 답안", path: routeMap.JobtestAnswerList }
            ]
        },
        {
            label: "지원자",
            role: ["인사팀"],
            children: [
                { label: "지원자 목록", path: "/employment/applicant" }
            ]
        },
        {
            label: "면접 관리",
            role: ["인사팀"],
            children: [
                { label: "면접 평가", path: "/employment/interviews" },
                { label: "면접 평가 기준표", path: "/employment/interview-criteria" }
            ]
        },
        {
            label: "안내 메일 관리",
            role: ["인사팀"],
            children: [
                { label: "안내 메일", path: "/employment/emails" },
                { label: "안내 메일 템플릿", path: "/employment/email-templates" }
            ]
        }
    ],
    일정: [
        {
            label: "공식 일정",
            role: ["인사팀"],
            children: []
        },
        {
            label: "부서일정",
            role: ["인사팀"],
            children: []
        }
    ]
}