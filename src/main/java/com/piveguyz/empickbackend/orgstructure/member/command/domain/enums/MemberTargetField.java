package com.piveguyz.empickbackend.orgstructure.member.command.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "사원 정보 수정 요청 대상 필드", enumAsRef = true)
public enum MemberTargetField {
    @Schema(description = "이름", example = "NAME")
    NAME("이름", FieldType.STRING),

    @Schema(description = "전화번호", example = "PHONE")
    PHONE("전화번호", FieldType.STRING),

    @Schema(description = "이메일", example = "EMAIL")
    EMAIL("이메일", FieldType.STRING),

    @Schema(description = "주소", example = "ADDRESS")
    ADDRESS("주소", FieldType.STRING),

    @Schema(description = "프로필사진", example = "PICTURE_URL")
    PICTURE_URL("프로필사진", FieldType.STRING),

    @Schema(description = "직급", example = "RANK")
    RANK("직급", FieldType.INT),

    @Schema(description = "직책", example = "POSITION")
    POSITION("직책", FieldType.INT),

    @Schema(description = "직무", example = "JOB")
    JOB("직무", FieldType.INT),

    @Schema(description = "부서", example = "DEPARTMENT")
    DEPARTMENT("부서", FieldType.INT);

    private final String description;
    private final FieldType fieldType;

    MemberTargetField(String description, FieldType fieldType) {
        this.description = description;
        this.fieldType = fieldType;
    }
}
