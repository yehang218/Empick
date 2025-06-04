package com.piveguyz.empickbackend.employment.recruitment.query.dto;

import lombok.Data;

@Data
public class RecruitmentQueryConditionDTO {
	private String keyword;
	private Integer recruitType;
	private String departmentName;
	private Integer status;
	private String startFrom;
	private String endTo;
}
