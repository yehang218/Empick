package com.piveguyz.empickbackend.orgstructure.job.command.domain.enums;

public enum IsActive {
    INACTIVE(0),
    ACTIVE(1);

    private final int value;

    IsActive(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static IsActive fromValue(int value) {
        for (IsActive status : IsActive.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("IsActive 값이 잘못되었습니다: " + value);
    }
}