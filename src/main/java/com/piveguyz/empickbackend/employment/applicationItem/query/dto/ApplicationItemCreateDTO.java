package com.piveguyz.empickbackend.employment.applicationItem.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationItemCreateDTO {
	private int applicationItemCategoryId;
	private boolean isRequired;
}
