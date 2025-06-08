package com.piveguyz.empickbackend.employment.applicationItem.query.dto;

import lombok.Getter;

@Getter
public enum InputType {
	TEXT(0),
	TEXTAREA(1),
	FILE(2),
	URL(3),
	DATE(4),
	NUMBER(5),
	RADIO(6),
	CHECKBOX(7);

	private final int code;

	InputType(int code) {
		this.code = code;
	}

	public static InputType fromCode(int code) {
		for (InputType type : InputType.values()) {
			if (type.code == code) return type;
		}
		throw new IllegalArgumentException("Unknown input type code: " + code);
	}
}
