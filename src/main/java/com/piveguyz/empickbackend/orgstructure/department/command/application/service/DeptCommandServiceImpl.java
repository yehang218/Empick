package com.piveguyz.empickbackend.orgstructure.department.command.application.service;

import com.piveguyz.empickbackend.common.enums.IsActive;
import com.piveguyz.empickbackend.orgstructure.department.command.application.dto.DeptCommandDTO;
import com.piveguyz.empickbackend.orgstructure.department.command.domain.aggregate.DepartmentEntity;
import com.piveguyz.empickbackend.orgstructure.department.command.domain.repository.DeptRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeptCommandServiceImpl implements DeptCommandService {

    private final DeptRepository deptRepository;

    @Override
    @Transactional
    public DeptCommandDTO createDept(@Valid DeptCommandDTO deptCommandDTO) {
        DepartmentEntity deptEntity = DepartmentEntity.builder()
                .name(deptCommandDTO.getName())
                .code(deptCommandDTO.getCode())
                .isActive(IsActive.fromValue(deptCommandDTO.getIsActive()))
                .description(deptCommandDTO.getDescription())
                .roleId(deptCommandDTO.getRoleId())
                .build();

        DepartmentEntity saved = deptRepository.save(deptEntity);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public DeptCommandDTO updateDept(int id, @Valid DeptCommandDTO deptCommandDTO) {
        DepartmentEntity deptEntity = deptRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("부서를 찾을 수 없습니다."));

        deptEntity.setName(deptCommandDTO.getName());
        deptEntity.setCode(deptCommandDTO.getCode());
        deptEntity.setIsActive(IsActive.fromValue(deptCommandDTO.getIsActive()));
        deptEntity.setDescription(deptCommandDTO.getDescription());
        deptEntity.setRoleId(deptCommandDTO.getRoleId());

        DepartmentEntity updated = deptRepository.save(deptEntity);
        return toDTO(updated);
    }

    @Override
    @Transactional
    public Integer updateDeptActiveStatus(int id, IsActive isActive) {
        DepartmentEntity deptEntity = deptRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("부서를 찾을 수 없습니다."));

        deptEntity.setIsActive(isActive);
        deptRepository.save(deptEntity);

        return deptEntity.getId();
    }

    // Entity → DTO 변환 메서드
    private DeptCommandDTO toDTO(DepartmentEntity entity) {
        return DeptCommandDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .isActive(entity.getIsActive().getValue())
                .description(entity.getDescription())
                .roleId(entity.getRoleId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}