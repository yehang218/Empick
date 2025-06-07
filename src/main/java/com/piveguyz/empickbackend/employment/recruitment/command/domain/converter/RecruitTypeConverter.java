package com.piveguyz.empickbackend.employment.recruitment.command.domain.converter;

import com.piveguyz.empickbackend.employment.recruitment.command.domain.enums.RecruitType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class RecruitTypeConverter implements AttributeConverter<RecruitType, Integer> {

	@Override
	public Integer convertToDatabaseColumn(RecruitType attribute) {
		if (attribute == null) return null;
		return attribute.getCode();  // 예: 0 or 1
	}

	@Override
	public RecruitType convertToEntityAttribute(Integer dbData) {
		if (dbData == null) return null;
		return RecruitType.fromCode(dbData); // 예: 0 -> REGULAR, 1 -> SEASONAL
	}
}