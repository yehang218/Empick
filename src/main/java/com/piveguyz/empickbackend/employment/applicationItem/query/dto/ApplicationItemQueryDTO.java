package com.piveguyz.empickbackend.employment.applicationItem.query.dto;

import com.piveguyz.empickbackend.common.enums.InputType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationItemQueryDTO {

	private int id;
	private int categoryId;
	private String categoryName;
	private InputType inputType;
	private boolean isRequired;
}
