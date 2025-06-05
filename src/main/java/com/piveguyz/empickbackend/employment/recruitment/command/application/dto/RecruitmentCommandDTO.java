package com.piveguyz.empickbackend.employment.recruitment.command.application.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RecruitmentCommandDTO {
	private String title;
	private String content;
	private int recruitType;
	private String imageUrl;
	private LocalDateTime startedAt;
	private LocalDateTime endedAt;
	private int memberId;
	private Integer recruitmentTemplateId;
	private int introduceTemplateId;
	private Integer recruitmentRequestId;
}
