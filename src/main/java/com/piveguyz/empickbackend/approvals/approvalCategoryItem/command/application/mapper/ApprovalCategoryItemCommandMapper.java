package com.piveguyz.empickbackend.approvals.approvalCategoryItem.command.application.mapper;

import com.piveguyz.empickbackend.approvals.approvalCategoryItem.command.application.dto.ApprovalCategoryItemCommandDTO;
import com.piveguyz.empickbackend.approvals.approvalCategoryItem.command.domain.aggregate.ApprovalCategoryItemEntity;
import org.springframework.stereotype.Component;

@Component
public class ApprovalCategoryItemCommandMapper {
    public ApprovalCategoryItemCommandDTO toDto(ApprovalCategoryItemEntity entity){
        ApprovalCategoryItemCommandDTO dto = new ApprovalCategoryItemCommandDTO();
        dto.setId(entity.getId());
        dto.setApplicationId(entity.getApplicationId());
        dto.setSheetId(entity.getSheetId());
        dto.setDatetime(entity.getDatetime());
        dto.setAddress(entity.getAddress());
        dto.setScore(entity.getScore());
        return dto;
    }

    public ApprovalCategoryItemEntity toEntity(ApprovalCategoryItemCommandDTO dto){
        ApprovalCategoryItemEntity entity = new ApprovalCategoryItemEntity();
        entity.setId(dto.getId());
        entity.setApplicationId(dto.getApplicationId());
        entity.setSheetId(dto.getSheetId());
        entity.setDatetime(dto.getDatetime());
        entity.setAddress(dto.getAddress());
        entity.setScore(dto.getScore());
        return entity;
    }
}
