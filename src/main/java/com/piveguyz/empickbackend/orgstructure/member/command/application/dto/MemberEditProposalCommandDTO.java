package com.piveguyz.empickbackend.orgstructure.member.command.application.dto;

import com.piveguyz.empickbackend.orgstructure.member.command.domain.enums.FieldType;
import com.piveguyz.empickbackend.orgstructure.member.command.domain.enums.MemberTargetField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberEditProposalCommandDTO {

    @Schema(description = "수정 대상 사원 ID", example = "1")
    private Integer memberId;

//    @Schema(description = "승인자 ID", example = "2")
//    private Integer approvedMemberId;

    @Schema(
            description = "수정하려는 필드명",
            example = "PHONE",
            allowableValues = {"NAME", "PHONE", "EMAIL", "ADDRESS", "PICTURE_URL", "RANK", "POSITION", "JOB", "DEPARTMENT"}
    )
    private MemberTargetField targetField;

    @Schema(description = "기존 값", example = "010-1234-5678")
    private String originalValue;

    @Schema(description = "요청 값", example = "010-5678-1234")
    private String requestedValue;

//    @Schema(
//            description = "필드 타입",
//            example = "STRING",
//            allowableValues = {"STRING", "INT", "DATETIME"}
//    )
//    private FieldType fieldType;

//    @Schema(
//            description = "요청 상태",
//            example = "PENDING",
//            allowableValues = {"PENDING", "APPROVED", "REJECTED"}
//    )
//    private String status;

    @Schema(description = "요청 시각", example = "2025-06-08T12:34:56")
    private LocalDateTime requestedAt;

    @Schema(description = "변경 사유", example = "연락처 변경 요청")
    private String reason;
}
