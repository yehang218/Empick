package com.piveguyz.empickbackend.employment.applicationItem.command.domain.aggregate;

import com.piveguyz.empickbackend.common.handler.YnBooleanConverter;
import com.piveguyz.empickbackend.employment.recruitment.command.domain.aggregate.Recruitment;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "application_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "is_required", nullable = false)
	@Convert(converter = YnBooleanConverter.class)
	private boolean isRequiredYn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "application_item_category_id")
	private ApplicationItemCategory category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recruitment_id")
	private Recruitment recruitment;
}
