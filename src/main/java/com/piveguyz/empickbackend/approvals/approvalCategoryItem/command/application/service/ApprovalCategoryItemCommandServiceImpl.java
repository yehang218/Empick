package com.piveguyz.empickbackend.approvals.approvalCategoryItem.command.application.service;

import com.piveguyz.empickbackend.approvals.approvalCategoryItem.command.application.dto.ApprovalCategoryItemCommandDTO;
import com.piveguyz.empickbackend.approvals.approvalCategoryItem.command.application.mapper.ApprovalCategoryItemCommandMapper;
import com.piveguyz.empickbackend.approvals.approvalCategoryItem.command.domain.aggregate.ApprovalCategoryItemEntity;
import com.piveguyz.empickbackend.approvals.approvalCategoryItem.command.domain.repository.ApprovalCategoryItemRepository;
import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalCategoryItemCommandServiceImpl implements ApprovalCategoryItemCommandService {
    private final ApprovalCategoryItemRepository repository;
    private final ApprovalCategoryItemCommandMapper mapper;

    @Override
    public ApprovalCategoryItemCommandDTO create(ApprovalCategoryItemCommandDTO dto) {
        ApprovalCategoryItemEntity entity = new ApprovalCategoryItemEntity();
        entity.setApplicationId(dto.getApplicationId());
        entity.setSheetId(dto.getSheetId());
        entity.setDatetime(dto.getDatetime());
        entity.setAddress(dto.getAddress());
        entity.setScore(dto.getScore());
        ApprovalCategoryItemEntity createdEntity = repository.save(entity);
        return mapper.toDto(createdEntity);
    }

    @Override
    public ApprovalCategoryItemCommandDTO update(Integer id, ApprovalCategoryItemCommandDTO dto) {
        ApprovalCategoryItemEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_NOT_FOUND));
        entity.setSheetId(dto.getSheetId());
        entity.setDatetime(dto.getDatetime());
        entity.setAddress(dto.getAddress());
        entity.setScore(dto.getScore());
        ApprovalCategoryItemEntity updatedEntity = repository.save(entity);
        return mapper.toDto(updatedEntity);
    }

    @Override
    public ApprovalCategoryItemCommandDTO updateDateTime(Integer id, LocalDateTime datetime) {
        ApprovalCategoryItemEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_NOT_FOUND));
        entity.setDatetime(datetime);
        ApprovalCategoryItemEntity updatedEntity = repository.save(entity);
        return mapper.toDto(updatedEntity);
    }

    @Override
    public ApprovalCategoryItemCommandDTO updateAddress(Integer id, String address) {
        ApprovalCategoryItemEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_NOT_FOUND));
        entity.setAddress(address);
        ApprovalCategoryItemEntity updatedEntity = repository.save(entity);
        return mapper.toDto(updatedEntity);
    }

    @Override
    public ApprovalCategoryItemCommandDTO delete(Integer id) {
        ApprovalCategoryItemEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_NOT_FOUND));
        repository.delete(entity);
        return mapper.toDto(entity);
    }
}
