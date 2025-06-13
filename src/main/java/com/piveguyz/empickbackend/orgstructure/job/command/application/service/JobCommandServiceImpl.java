package com.piveguyz.empickbackend.orgstructure.job.command.application.service;

import com.piveguyz.empickbackend.common.enums.IsActive;
import com.piveguyz.empickbackend.orgstructure.job.command.application.dto.JobCommandDTO;
import com.piveguyz.empickbackend.orgstructure.job.command.domain.aggregate.JobEntity;
import com.piveguyz.empickbackend.orgstructure.job.command.domain.repository.JobRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JobCommandServiceImpl implements JobCommandService {

    private final JobRepository jobRepository;

    @Override
    @Transactional
    public JobCommandDTO createJob(@Valid JobCommandDTO jobCommandDTO) {
        JobEntity jobEntity = JobEntity.builder()
                .name(jobCommandDTO.getName())
                .code(jobCommandDTO.getCode())
                .isActive(IsActive.fromValue(jobCommandDTO.getIsActive()))
                .description(jobCommandDTO.getDescription())
                .roleId(jobCommandDTO.getRoleId())
                .build();

        JobEntity savedJob = jobRepository.save(jobEntity);

        return toDTO(savedJob);
    }

    @Override
    @Transactional
    public JobCommandDTO updateJob(int id, @Valid JobCommandDTO jobCommandDTO) {
        JobEntity job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("직무를 찾을 수 없습니다."));

        job.setName(jobCommandDTO.getName());
        job.setCode(jobCommandDTO.getCode());
        job.setIsActive(IsActive.fromValue(jobCommandDTO.getIsActive()));
        job.setDescription(jobCommandDTO.getDescription());
        job.setRoleId(jobCommandDTO.getRoleId());

        JobEntity updatedJob = jobRepository.save(job);

        return toDTO(updatedJob);
    }

    @Override
    @Transactional
    public Integer updateJobActiveStatus(int id, IsActive isActive) {
        JobEntity job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("직무를 찾을 수 없습니다."));

        // 인자로 들어오는 isActive가 이미 IsActive 타입이므로, fromValue 호출 없이 바로 할당
        job.setIsActive(isActive);
        jobRepository.save(job);

        return job.getId();
    }

    // Entity -> DTO 변환 헬퍼 메서드
    private JobCommandDTO toDTO(JobEntity job) {
        return JobCommandDTO.builder()
                .name(job.getName())
                .code(job.getCode())
                .isActive(job.getIsActive().getValue())
                .description(job.getDescription())
                .roleId(job.getRoleId())
                .build();
    }
}