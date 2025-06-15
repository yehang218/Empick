package com.piveguyz.empickbackend.approvals.approval.command.application.mapper;

import com.piveguyz.empickbackend.approvals.approval.command.application.dto.ApprovalCommandDTO;
import com.piveguyz.empickbackend.approvals.approval.command.domain.aggregate.ApprovalEntity;
import org.springframework.stereotype.Component;

@Component
public class ApprovalCommandMapper {
    public ApprovalCommandDTO toDto(ApprovalEntity entity){
        ApprovalCommandDTO dto = new ApprovalCommandDTO();
        dto.setId(entity.getId());
        dto.setApplicationId(entity.getApplicationId());
        dto.setSheetId(entity.getSheetId());
        dto.setDatetime(entity.getDatetime());
        dto.setAddress(entity.getAddress());
        dto.setScore(entity.getScore());
        return dto;
    }

    public ApprovalEntity toEntity(ApprovalCommandDTO dto){
        ApprovalEntity entity = new ApprovalEntity();
        entity.setId(dto.getId());
        entity.setApplicationId(dto.getApplicationId());
        entity.setSheetId(dto.getSheetId());
        entity.setDatetime(dto.getDatetime());
        entity.setAddress(dto.getAddress());
        entity.setScore(dto.getScore());
        return entity;
    }
}
