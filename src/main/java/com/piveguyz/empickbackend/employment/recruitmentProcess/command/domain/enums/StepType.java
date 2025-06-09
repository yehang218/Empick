package com.piveguyz.empickbackend.employment.recruitmentProcess.command.domain.enums;

public enum StepType {
	DOCUMENT(0, "서류"),
	PRACTICAL(1, "실무"),
	INTERVIEW(2, "면접");

	private final int code;
	private final String label;

	StepType(int code, String label) {
		this.code = code;
		this.label = label;
	}

	public int getCode() {
		return code;
	}

	public String getLabel() {
		return label;
	}

	public static StepType fromCode(int code) {
		for (StepType type : StepType.values()) {
			if (type.code == code) return type;
		}
		throw new IllegalArgumentException("Invalid StepType code: " + code);
	}
}