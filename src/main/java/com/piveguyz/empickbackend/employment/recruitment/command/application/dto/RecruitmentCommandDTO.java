package com.piveguyz.empickbackend.employment.recruitment.command.application.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.piveguyz.empickbackend.employment.applicationItem.query.dto.ApplicationItemCreateDTO;
import com.piveguyz.empickbackend.employment.recruitment.command.domain.enums.RecruitType;
import com.piveguyz.empickbackend.employment.recruitmentProcess.command.application.dto.RecruitmentProcessCreateDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentCommandDTO {
	private String title;
	private String content;
	private RecruitType recruitType;
	private String imageUrl;
	private LocalDateTime startedAt;
	private LocalDateTime endedAt;
	private int memberId;
	private Integer recruitmentTemplateId;
	private int introduceTemplateId;
	private Integer recruitmentRequestId;

	private List<ApplicationItemCreateDTO> applicationItems; // 지원서 항목
	private List<RecruitmentProcessCreateDTO> recruitmentProcesses; // 채용 프로세스
}
