package com.piveguyz.empickbackend.employment.recruitmentTemplateCopy.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentTemplateCopyQueryDTO {
	private int id;
	private String title;
	private String content;
	private int displayOrder;
	private int recruitmentId;
	private int recruitmentTemplateItemId;
}