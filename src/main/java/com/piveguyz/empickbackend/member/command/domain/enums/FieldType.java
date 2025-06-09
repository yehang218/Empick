package com.piveguyz.empickbackend.member.command.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "수정 요청 타입 (0 = STRING, 1 = INT, 2 = DATETIME)", enumAsRef = true)
public enum FieldType {
    @Schema(description = "문자열", example = "STRING")
    STRING(0),

    @Schema(description = "숫자", example = "INT")
    INT(1),

    @Schema(description = "날짜/시간", example = "DATETIME")
    DATETIME(2);

    private final int code;

    FieldType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static FieldType fromCode(int code) {
        return switch (code) {
            case 0 -> STRING;
            case 1 -> INT;
            case 2 -> DATETIME;
            default -> throw new IllegalArgumentException("Invalid field type code: " + code);
        };
    }
}
