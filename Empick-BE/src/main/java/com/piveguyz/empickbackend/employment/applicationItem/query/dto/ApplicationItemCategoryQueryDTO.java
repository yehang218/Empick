package com.piveguyz.empickbackend.employment.applicationItem.query.dto;

import com.piveguyz.empickbackend.common.enums.InputType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationItemCategoryQueryDTO {
	private final int id;
	private final String name;
	private final InputType inputType;
	private final int displayOrder;
	private final Integer applicationItemCategoryId;
}
