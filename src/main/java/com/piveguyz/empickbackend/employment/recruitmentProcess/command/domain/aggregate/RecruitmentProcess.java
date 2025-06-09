package com.piveguyz.empickbackend.employment.recruitmentProcess.command.domain.aggregate;

import com.piveguyz.empickbackend.employment.recruitment.command.domain.aggregate.Recruitment;
import com.piveguyz.empickbackend.employment.recruitmentProcess.command.domain.enums.StepType;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recruitment_process")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RecruitmentProcess {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "step_type", nullable = false)
	private StepType stepType;

	@Column(name = "display_order", nullable = false)
	private int displayOrder;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recruitment_id", nullable = false)
	private Recruitment recruitment;

	public static RecruitmentProcess create(StepType stepType, int displayOrder, Recruitment recruitment) {
		return RecruitmentProcess.builder()
			.stepType(stepType)
			.displayOrder(displayOrder)
			.recruitment(recruitment)
			.build();
	}
}