package com.piveguyz.empickbackend.approvals.approvalCategory.command.application.service;

import com.piveguyz.empickbackend.approvals.approvalCategory.command.application.dto.ApprovalCategoryCommandDTO;
import com.piveguyz.empickbackend.approvals.approvalCategory.command.application.mapper.ApprovalCategoryCommandMapper;
import com.piveguyz.empickbackend.approvals.approvalCategory.command.domain.aggregate.ApprovalCategoryEntity;
import com.piveguyz.empickbackend.approvals.approvalCategory.command.domain.repository.ApprovalCategoryRepository;
import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalCategoryCommandServiceImpl implements ApprovalCategoryCommandService {
    private final ApprovalCategoryRepository repository;
    private final ApprovalCategoryCommandMapper mapper;

    @Override
    public ApprovalCategoryCommandDTO create(ApprovalCategoryCommandDTO dto) {
        ApprovalCategoryEntity entity = new ApprovalCategoryEntity();
        entity.setApplicationId(dto.getApplicationId());
        entity.setSheetId(dto.getSheetId());
        entity.setDatetime(dto.getDatetime());
        entity.setAddress(dto.getAddress());
        entity.setScore(dto.getScore());
        ApprovalCategoryEntity createdEntity = repository.save(entity);
        return mapper.toDto(createdEntity);
    }

    @Override
    public ApprovalCategoryCommandDTO update(Integer id, ApprovalCategoryCommandDTO dto) {
        ApprovalCategoryEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_NOT_FOUND));
        entity.setSheetId(dto.getSheetId());
        entity.setDatetime(dto.getDatetime());
        entity.setAddress(dto.getAddress());
        entity.setScore(dto.getScore());
        ApprovalCategoryEntity updatedEntity = repository.save(entity);
        return mapper.toDto(updatedEntity);
    }

    @Override
    public ApprovalCategoryCommandDTO updateDateTime(Integer id, LocalDateTime datetime) {
        ApprovalCategoryEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_NOT_FOUND));
        entity.setDatetime(datetime);
        ApprovalCategoryEntity updatedEntity = repository.save(entity);
        return mapper.toDto(updatedEntity);
    }

    @Override
    public ApprovalCategoryCommandDTO updateAddress(Integer id, String address) {
        ApprovalCategoryEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_NOT_FOUND));
        entity.setAddress(address);
        ApprovalCategoryEntity updatedEntity = repository.save(entity);
        return mapper.toDto(updatedEntity);
    }

    @Override
    public ApprovalCategoryCommandDTO delete(Integer id) {
        ApprovalCategoryEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_NOT_FOUND));
        repository.delete(entity);
        return mapper.toDto(entity);
    }
}
