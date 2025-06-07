package com.piveguyz.empickbackend.employment.applicationItem.command.domain.aggregate;

import com.piveguyz.empickbackend.employment.applicationItem.query.dto.InputType;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "application_item_category")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationItemCategory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "input_type")
	private InputType inputType;

	@Column(name = "display_order")
	private int displayOrder;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "application_item_category_id")
	private ApplicationItemCategory parentCategory;
}