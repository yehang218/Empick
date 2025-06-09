package com.piveguyz.empickbackend.employment.recruitment.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.piveguyz.empickbackend.employment.recruitment.command.application.dto.RecruitmentCommandDTO;
import com.piveguyz.empickbackend.employment.recruitment.command.domain.converter.RecruitTypeConverter;
import com.piveguyz.empickbackend.employment.recruitment.command.domain.converter.RecruitmentStatusConverter;
import com.piveguyz.empickbackend.employment.recruitment.command.domain.enums.RecruitType;
import com.piveguyz.empickbackend.employment.recruitment.command.domain.enums.RecruitmentStatus;

@Entity
@Table(name = "recruitment")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Recruitment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String title;

	@Lob
	private String content;

	@Convert(converter = RecruitTypeConverter.class)
	@Column(name = "recruit_type")
	private RecruitType recruitType;

	@Convert(converter = RecruitmentStatusConverter.class)
	@Column(name = "status", nullable = false)
	private RecruitmentStatus status;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "started_at")
	private LocalDateTime startedAt;

	@Column(name = "ended_at")
	private LocalDateTime endedAt;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Column(name = "member_id")
	private int memberId;

	@Column(name = "recruitment_template_id")
	private Integer recruitmentTemplateId;

	@Column(name = "introduce_template_id")
	private int introduceTemplateId;

	@Column(name = "recruitment_request_id")
	private Integer recruitmentRequestId;

	public void update(RecruitmentCommandDTO dto) {
		this.title = dto.getTitle();
		this.content = dto.getContent();
		this.recruitType = dto.getRecruitType();
		this.imageUrl = dto.getImageUrl();
		this.startedAt = dto.getStartedAt();
		this.endedAt = dto.getEndedAt();
		this.recruitmentTemplateId = dto.getRecruitmentTemplateId();
		this.introduceTemplateId = dto.getIntroduceTemplateId();
		this.recruitmentRequestId = dto.getRecruitmentRequestId();
	}

}
