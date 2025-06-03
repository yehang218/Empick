package com.piveguyz.empickbackend.employment.recruitmentRequest.query.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RecruitmentRequestQueryDTO {
	private int id;
	private int headcount;
	private LocalDateTime started_at;
	private LocalDateTime ended_at;
	private String qualification;
	private String preference;
	private String responsibility;
	private String employment_type;
	private String work_location;
	private LocalDateTime created_at;
	private int member_id;
	private int department_id;
}
