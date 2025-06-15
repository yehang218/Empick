package com.piveguyz.empickbackend.approvals.approvalContent.command.application.mapper;

import com.piveguyz.empickbackend.approvals.approvalContent.command.application.dto.ApprovalContentCommandDTO;
import com.piveguyz.empickbackend.approvals.approvalContent.command.domain.aggregate.ApprovalContentEntity;
import org.springframework.stereotype.Component;

@Component
public class ApprovalContentCommandMapper {
    public ApprovalContentCommandDTO toDto(ApprovalContentEntity entity){
        ApprovalContentCommandDTO dto = new ApprovalContentCommandDTO();
        dto.setId(entity.getId());
        dto.setApplicationId(entity.getApplicationId());
        dto.setSheetId(entity.getSheetId());
        dto.setDatetime(entity.getDatetime());
        dto.setAddress(entity.getAddress());
        dto.setScore(entity.getScore());
        return dto;
    }

    public ApprovalContentEntity toEntity(ApprovalContentCommandDTO dto){
        ApprovalContentEntity entity = new ApprovalContentEntity();
        entity.setId(dto.getId());
        entity.setApplicationId(dto.getApplicationId());
        entity.setSheetId(dto.getSheetId());
        entity.setDatetime(dto.getDatetime());
        entity.setAddress(dto.getAddress());
        entity.setScore(dto.getScore());
        return entity;
    }
}
