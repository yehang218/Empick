package com.piveguyz.empickbackend.employment.recruitment.command.domain.enums;

public enum RecruitType {
	REGULAR(0), // 상시
	SEASONAL(1); // 공채

	private final int code;

	RecruitType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static RecruitType fromCode(int code) {
		for (RecruitType type : RecruitType.values()) {
			if (type.code == code) return type;
		}
		throw new IllegalArgumentException("Invalid RecruitType code: " + code);
	}
}
