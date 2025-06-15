package com.piveguyz.empickbackend.approvals.approval.command.application.service;

import com.piveguyz.empickbackend.approvals.approval.command.application.dto.ApprovalCommandDTO;
import com.piveguyz.empickbackend.approvals.approval.command.application.mapper.ApprovalCommandMapper;
import com.piveguyz.empickbackend.approvals.approval.command.domain.aggregate.ApprovalEntity;
import com.piveguyz.empickbackend.approvals.approval.command.domain.repository.ApprovalRepository;
import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalCommandServiceImpl implements ApprovalCommandService {
    private final ApprovalRepository repository;
    private final ApprovalCommandMapper mapper;

    @Override
    public ApprovalCommandDTO create(ApprovalCommandDTO dto) {
        ApprovalEntity entity = new ApprovalEntity();
        entity.setApplicationId(dto.getApplicationId());
        entity.setSheetId(dto.getSheetId());
        entity.setDatetime(dto.getDatetime());
        entity.setAddress(dto.getAddress());
        entity.setScore(dto.getScore());
        ApprovalEntity createdEntity = repository.save(entity);
        return mapper.toDto(createdEntity);
    }

    @Override
    public ApprovalCommandDTO update(Integer id, ApprovalCommandDTO dto) {
        ApprovalEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_NOT_FOUND));
        entity.setSheetId(dto.getSheetId());
        entity.setDatetime(dto.getDatetime());
        entity.setAddress(dto.getAddress());
        entity.setScore(dto.getScore());
        ApprovalEntity updatedEntity = repository.save(entity);
        return mapper.toDto(updatedEntity);
    }

    @Override
    public ApprovalCommandDTO updateDateTime(Integer id, LocalDateTime datetime) {
        ApprovalEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_NOT_FOUND));
        entity.setDatetime(datetime);
        ApprovalEntity updatedEntity = repository.save(entity);
        return mapper.toDto(updatedEntity);
    }

    @Override
    public ApprovalCommandDTO updateAddress(Integer id, String address) {
        ApprovalEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_NOT_FOUND));
        entity.setAddress(address);
        ApprovalEntity updatedEntity = repository.save(entity);
        return mapper.toDto(updatedEntity);
    }

    @Override
    public ApprovalCommandDTO delete(Integer id) {
        ApprovalEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_NOT_FOUND));
        repository.delete(entity);
        return mapper.toDto(entity);
    }
}
