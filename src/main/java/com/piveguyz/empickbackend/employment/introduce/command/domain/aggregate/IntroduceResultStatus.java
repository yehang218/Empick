package com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate;


public enum IntroduceResultStatus {
    BEFORE_CONFIRM(0, "확인 전"),
    CONFIRMED(1, "확인만"),
    PASS(2, "합격"),
    FAIL(3, "불합격");

    private final int code;
    private final String description;

    IntroduceResultStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static IntroduceResultStatus fromCode(int code) {
        for (IntroduceResultStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid IntroduceResultStatus code: " + code);
    }
}

