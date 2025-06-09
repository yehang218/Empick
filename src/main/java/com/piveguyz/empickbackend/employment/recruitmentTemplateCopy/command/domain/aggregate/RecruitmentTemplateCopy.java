package com.piveguyz.empickbackend.employment.recruitmentTemplateCopy.command.domain.aggregate;

import com.piveguyz.empickbackend.employment.recruitment.command.domain.aggregate.Recruitment;
import com.piveguyz.empickbackend.employment.recruitmentTemplate.command.domain.aggregate.RecruitmentTemplateItem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recruitment_template_copy")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecruitmentTemplateCopy {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "title", nullable = false)
	private String title;

	@Lob
	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "display_order", nullable = false)
	private Integer displayOrder;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recruitment_id", nullable = false)
	private Recruitment recruitment;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recruitment_template_item_id", nullable = false)
	private RecruitmentTemplateItem recruitmentTemplateItem;
}
