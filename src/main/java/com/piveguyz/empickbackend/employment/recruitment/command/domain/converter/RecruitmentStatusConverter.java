package com.piveguyz.empickbackend.employment.recruitment.command.domain.converter;

import com.piveguyz.empickbackend.employment.recruitment.command.domain.enums.RecruitmentStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class RecruitmentStatusConverter implements AttributeConverter<RecruitmentStatus, Integer> {
	@Override
	public Integer convertToDatabaseColumn(RecruitmentStatus attribute) {
		return attribute != null ? attribute.getCode() : null;
	}

	@Override
	public RecruitmentStatus convertToEntityAttribute(Integer dbData) {
		return dbData != null ? RecruitmentStatus.fromCode(dbData) : null;
	}
}