package com.piveguyz.empickbackend.employment.recruitmentRequest.query.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentRequestQueryDTO {
	private int id;
	private int headcount;
	private LocalDateTime startedAt;
	private LocalDateTime endedAt;
	private String qualification;
	private String preference;
	private String responsibility;
	private String employmentType;
	private String workLocation;
	private LocalDateTime createdAt;
	private int memberId;
	private int departmentId;
	private String memberName;
	private String departmentName;
	private int jobId;
	private String jobName;
}
