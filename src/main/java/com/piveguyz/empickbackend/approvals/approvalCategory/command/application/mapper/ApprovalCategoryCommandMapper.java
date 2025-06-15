package com.piveguyz.empickbackend.approvals.approvalCategory.command.application.mapper;

import com.piveguyz.empickbackend.approvals.approvalCategory.command.application.dto.ApprovalCategoryCommandDTO;
import com.piveguyz.empickbackend.approvals.approvalCategory.command.domain.aggregate.ApprovalCategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class ApprovalCategoryCommandMapper {
    public ApprovalCategoryCommandDTO toDto(ApprovalCategoryEntity entity){
        ApprovalCategoryCommandDTO dto = new ApprovalCategoryCommandDTO();
        dto.setId(entity.getId());
        dto.setApplicationId(entity.getApplicationId());
        dto.setSheetId(entity.getSheetId());
        dto.setDatetime(entity.getDatetime());
        dto.setAddress(entity.getAddress());
        dto.setScore(entity.getScore());
        return dto;
    }

    public ApprovalCategoryEntity toEntity(ApprovalCategoryCommandDTO dto){
        ApprovalCategoryEntity entity = new ApprovalCategoryEntity();
        entity.setId(dto.getId());
        entity.setApplicationId(dto.getApplicationId());
        entity.setSheetId(dto.getSheetId());
        entity.setDatetime(dto.getDatetime());
        entity.setAddress(dto.getAddress());
        entity.setScore(dto.getScore());
        return entity;
    }
}
