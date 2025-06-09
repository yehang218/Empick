package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum JobtestStatus {
    WAITING(0),
    IN_PROGRESS(1),
    COMPLETED(2);

    private final int code;

    JobtestStatus(int code) {
        this.code = code;
    }

    public static JobtestStatus fromCode(int code) {
        return Arrays.stream(JobtestStatus.values())
                .filter(d -> d.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid code: " + code));
    }
}
