package com.piveguyz.empickbackend.employment.recruitmentProcess.query.dto;

import com.piveguyz.empickbackend.employment.recruitmentProcess.command.domain.enums.StepType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecruitmentProcessQueryDTO {
	private StepType stepType;
	private String label;
	private int displayOrder;

	public void setStepType(StepType stepType) {
		this.stepType = stepType;
		this.label = stepType.getLabel();
	}
}