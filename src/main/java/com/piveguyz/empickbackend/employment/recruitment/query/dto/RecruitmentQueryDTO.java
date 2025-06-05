package com.piveguyz.empickbackend.employment.recruitment.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentQueryDTO {
	private int id;
	private String title;
	private String content;
	private int recruitType;        // 0: 상시, 1: 공채
	private int status;             // 0: 대기, 1: 승인, 2: 게시, 3: 종료
	private String imageUrl;
	private String startedAt;
	private String endedAt;
	private String createdAt;
	private String updatedAt;
	private String deletedAt;
	private int memberId;
	private String memberName;      // 작성자
	private String departmentName;  // 부서명
	private Integer recruitmentTemplateId;
	private int introduceTemplateId;
	private Integer recruitmentRequestId;
}
