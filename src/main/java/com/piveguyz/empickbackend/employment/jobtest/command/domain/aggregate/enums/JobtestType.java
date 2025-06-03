package com.piveguyz.empickbackend.employment.jobtest.command.domain.aggregate.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum JobtestType {
    MULTIPLE(0),        // 선택형
    SUBJECTIVE(1),      // 주관식
    DESCRIPTIVE(2);     // 서술형

    private final int code;

    JobtestType(int code) {
        this.code = code;
    }

    public static JobtestType fromCode(int code) {
        return Arrays.stream(JobtestType.values())
                .filter(d -> d.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid difficulty code: " + code)); // 🚩 Exception Handler로 수정해야 함
    }

}
