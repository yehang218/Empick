package com.piveguyz.empickbackend.approvals.approval.command.domain.enums;

public enum ApprovalStatus {
    REJECTED(-1, "반려"),
    IN_PROGRESS(0, "결재 진행중"),
    APPROVED(1, "결재 완료");

    private final int code;
    private final String description;

    ApprovalStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ApprovalStatus fromCode(int code) {
        for (ApprovalStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}