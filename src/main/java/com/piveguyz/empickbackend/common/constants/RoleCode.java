package com.piveguyz.empickbackend.common.constants;

public enum RoleCode {
    ROLE_HR_ACCESS("인사팀 기능 접근"),
    ROLE_RECRUITMENT_PLAN_EDITOR("채용 계획서 작성"),
    ROLE_APPROVAL_PROCESSOR("결재 처리 권한"),
    ROLE_RECRUITMENT_OPERATOR("채용 진행자"),
    ROLE_USER("일반 사용자");

    private final String description;

    RoleCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
