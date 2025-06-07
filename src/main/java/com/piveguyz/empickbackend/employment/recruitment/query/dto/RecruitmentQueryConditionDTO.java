package com.piveguyz.empickbackend.employment.recruitment.query.dto;

import com.piveguyz.empickbackend.employment.recruitment.command.domain.enums.RecruitType;
import com.piveguyz.empickbackend.employment.recruitment.command.domain.enums.RecruitmentStatus;

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
	private RecruitType recruitType;
	private String departmentName;
	private RecruitmentStatus status;
	private String startFrom;
	private String endTo;
}
