package com.piveguyz.empickbackend.employment.recruitment.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentQueryConditionDTO {
	private String keyword;
	private Integer recruitType;
	private String departmentName;
	private Integer status;
	private String startFrom;
	private String endTo;
}
