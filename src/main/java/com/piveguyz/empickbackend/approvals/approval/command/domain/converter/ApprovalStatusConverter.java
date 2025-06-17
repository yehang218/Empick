package com.piveguyz.empickbackend.approvals.approval.command.domain.converter;

import com.piveguyz.empickbackend.approvals.approval.command.domain.enums.ApprovalStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ApprovalStatusConverter implements AttributeConverter<ApprovalStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ApprovalStatus status) {
        if (status == null) return null;
        return status.getCode();
    }

    @Override
    public ApprovalStatus convertToEntityAttribute(Integer code) {
        if (code == null) return null;
        return ApprovalStatus.fromCode(code);
    }
}