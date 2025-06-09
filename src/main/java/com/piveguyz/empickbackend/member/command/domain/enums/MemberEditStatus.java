package com.piveguyz.empickbackend.member.command.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사원 정보 수정 요청 상태 (0 = PENDING, 1 = APPROVED, 2 = REJECTED)", enumAsRef = true)
public enum MemberEditStatus {
    @Schema(description = "대기 상태", example = "PENDING")
    PENDING(0),

    @Schema(description = "승인됨", example = "APPROVED")
    APPROVED(1),

    @Schema(description = "거부됨", example = "REJECTED")
    REJECTED(2);

    private final int code;

    MemberEditStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static MemberEditStatus fromCode(int code) {
        return switch (code) {
            case 0 -> PENDING;
            case 1 -> APPROVED;
            case 2 -> REJECTED;
            default -> throw new IllegalArgumentException("Invalid status code: " + code);
        };
    }
}
