package com.piveguyz.empickbackend.orgstructure.job.command.application.service;

import com.piveguyz.empickbackend.common.enums.IsActive;
import com.piveguyz.empickbackend.orgstructure.job.command.application.dto.JobCommandDTO;
import jakarta.validation.Valid;

public interface JobCommandService {

    // 직무 등록
    JobCommandDTO createJob(@Valid JobCommandDTO jobCommandDTO);

    // 직무 수정 (필요시 추가)
    JobCommandDTO updateJob(int id, @Valid JobCommandDTO jobCommandDTO);

    // 직무 삭제 (필요시 추가)
    Integer updateJobActiveStatus(int id, IsActive isActive);
}