package com.piveguyz.empickbackend.employment.recruitmentRequest.command.application.service;

import static com.piveguyz.empickbackend.common.response.ResponseCode.*;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.employment.recruitmentRequest.command.application.dto.RecruitmentRequestCommandDTO;
import com.piveguyz.empickbackend.employment.recruitmentRequest.command.domain.aggregate.RecruitmentRequest;
import com.piveguyz.empickbackend.employment.recruitmentRequest.command.domain.repository.RecruitmentRequestRepository;
import com.piveguyz.empickbackend.orgstructure.job.command.domain.repository.JobRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitmentRequestCommandServiceImpl implements RecruitmentRequestCommandService {

	private final RecruitmentRequestRepository recruitmentRequestRepository;
	private final JobRepository jobRepository;
	// private final DepartmentRepository departmentRepository;

	@Override
	public void create(RecruitmentRequestCommandDTO dto) {
		// 직무 존재 여부 확인
		if (!jobRepository.existsById(dto.getJobId())) {
			throw new BusinessException(EMPLOYMENT_REQUEST_INVALID_JOB_ID);
		}

		// 부서 존재 여부 확인
		// if (!departmentRepository.existsById(dto.getDepartmentId())) {
		// 	throw new BusinessException(EMPLOYMENT_REQUEST_INVALID_DEPARTMENT_ID);
		// }

		RecruitmentRequest request = RecruitmentRequest.builder()
			.headcount(dto.getHeadcount())
			.startedAt(dto.getStartedAt())
			.endedAt(dto.getEndedAt())
			.qualification(dto.getQualification())
			.preference(dto.getPreference())
			.responsibility(dto.getResponsibility())
			.employmentType(dto.getEmploymentType())
			.workLocation(dto.getWorkLocation())
			.memberId(dto.getMemberId())
			.departmentId(dto.getDepartmentId())
			.jobId(dto.getJobId())
			.createdAt(LocalDateTime.now())
			.build();

		recruitmentRequestRepository.save(request);
	}
}
