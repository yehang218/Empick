package com.piveguyz.empickbackend.approvals.approvalContent.command.application.service;

import com.piveguyz.empickbackend.approvals.approvalContent.command.application.dto.ApprovalContentCommandDTO;
import com.piveguyz.empickbackend.approvals.approvalContent.command.application.mapper.ApprovalContentCommandMapper;
import com.piveguyz.empickbackend.approvals.approvalContent.command.domain.aggregate.ApprovalContentEntity;
import com.piveguyz.empickbackend.approvals.approvalContent.command.domain.repository.ApprovalContentRepository;
import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalContentCommandServiceImpl implements ApprovalContentCommandService {
    private final ApprovalContentRepository repository;
    private final ApprovalContentCommandMapper mapper;

    @Override
    public ApprovalContentCommandDTO create(ApprovalContentCommandDTO dto) {
        ApprovalContentEntity entity = new ApprovalContentEntity();
        entity.setApplicationId(dto.getApplicationId());
        entity.setSheetId(dto.getSheetId());
        entity.setDatetime(dto.getDatetime());
        entity.setAddress(dto.getAddress());
        entity.setScore(dto.getScore());
        ApprovalContentEntity createdEntity = repository.save(entity);
        return mapper.toDto(createdEntity);
    }

    @Override
    public ApprovalContentCommandDTO update(Integer id, ApprovalContentCommandDTO dto) {
        ApprovalContentEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_NOT_FOUND));
        entity.setSheetId(dto.getSheetId());
        entity.setDatetime(dto.getDatetime());
        entity.setAddress(dto.getAddress());
        entity.setScore(dto.getScore());
        ApprovalContentEntity updatedEntity = repository.save(entity);
        return mapper.toDto(updatedEntity);
    }

    @Override
    public ApprovalContentCommandDTO updateDateTime(Integer id, LocalDateTime datetime) {
        ApprovalContentEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_NOT_FOUND));
        entity.setDatetime(datetime);
        ApprovalContentEntity updatedEntity = repository.save(entity);
        return mapper.toDto(updatedEntity);
    }

    @Override
    public ApprovalContentCommandDTO updateAddress(Integer id, String address) {
        ApprovalContentEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_NOT_FOUND));
        entity.setAddress(address);
        ApprovalContentEntity updatedEntity = repository.save(entity);
        return mapper.toDto(updatedEntity);
    }

    @Override
    public ApprovalContentCommandDTO delete(Integer id) {
        ApprovalContentEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_NOT_FOUND));
        repository.delete(entity);
        return mapper.toDto(entity);
    }
}
