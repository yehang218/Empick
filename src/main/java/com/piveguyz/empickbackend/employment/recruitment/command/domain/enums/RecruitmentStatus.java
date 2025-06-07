package com.piveguyz.empickbackend.employment.recruitment.command.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecruitmentStatus {
	WAITING(0),    // 대기
	APPROVED(1),   // 승인
	PUBLISHED(2),  // 게시
	CLOSED(3);     // 종료

	private final int code;

	public static RecruitmentStatus fromCode(int code) {
		for (RecruitmentStatus status : values()) {
			if (status.code == code) return status;
		}
		throw new IllegalArgumentException("Unknown status code: " + code);
	}
}