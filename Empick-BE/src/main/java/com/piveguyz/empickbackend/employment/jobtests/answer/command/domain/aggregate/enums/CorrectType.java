package com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.aggregate.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CorrectType {
    WRONG(0),
    PARTIAL(1),
    CORRECT(2);

    private final int code;

    CorrectType(int code) {
        this.code = code;
    }

    public static CorrectType fromCode(int code) {
        return Arrays.stream(CorrectType.values())
                .filter(d -> d.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid correct type: " + code));
    }
}
