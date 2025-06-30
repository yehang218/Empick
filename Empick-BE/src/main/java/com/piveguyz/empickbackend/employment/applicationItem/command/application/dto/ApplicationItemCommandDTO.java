package com.piveguyz.empickbackend.employment.applicationItem.command.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationItemCommandDTO {
	private int categoryId;
	private Boolean isRequired;
}
