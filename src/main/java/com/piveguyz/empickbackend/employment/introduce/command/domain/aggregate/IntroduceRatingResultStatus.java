package com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate;


import lombok.Getter;

@Getter
public enum IntroduceRatingResultStatus {
    PENDING(0, "확인전"),
    REVIEWING(1, "확인중"),
    PASSED(2, "합격"),
    FAILED(3, "불합격");

    private final int code;
    private final String label;

    IntroduceRatingResultStatus(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static IntroduceRatingResultStatus fromCode(int code) {
        for (IntroduceRatingResultStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid IntroduceRatingResultStatus code: " + code);
    }
}