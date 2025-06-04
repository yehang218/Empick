package com.piveguyz.empickbackend.employment.recruitment.command.application.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.recruitment.command.application.dto.RecruitmentCommandDTO;
import com.piveguyz.empickbackend.employment.recruitment.command.domain.aggregate.Recruitment;
import com.piveguyz.empickbackend.employment.recruitment.command.domain.repository.RecruitmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RecruitmentCommandServiceImpl implements RecruitmentCommandService {
	private final RecruitmentRepository recruitmentRepository;

	@Override
	public void createRecruitment(RecruitmentCommandDTO dto) {
		validate(dto);

		Recruitment recruitment = Recruitment.builder()
			.title(dto.getTitle())
			.content(dto.getContent())
			.recruitType(dto.getRecruitType())
			.imageUrl(dto.getImageUrl())
			.startedAt(dto.getStartedAt())
			.endedAt(dto.getEndedAt())
			.createdAt(LocalDateTime.now())
			.status(0) // 대기 상태
			.memberId(dto.getMemberId())
			.recruitmentTemplateId(dto.getRecruitmentTemplateId())
			.introduceTemplateId(dto.getIntroduceTemplateId())
			.recruitmentRequestId(dto.getRecruitmentRequestId())
			.build();

		recruitmentRepository.save(recruitment);
	}

	@Override
	public void updateRecruitment(int id, RecruitmentCommandDTO dto) {
		Recruitment recruitment = recruitmentRepository.findById(id)
			.orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_RECRUITMENT_NOT_FOUND));

		if (recruitment.getStatus() == 2) {
			throw new BusinessException(ResponseCode.EMPLOYMENT_RECRUITMENT_CANNOT_MODIFY_PUBLISHED);
		}

		validate(dto);

		recruitment.update(dto);
	}

	private void validate(RecruitmentCommandDTO dto) {
		if (dto.getTitle() == null || dto.getTitle().isBlank()) {
			throw new BusinessException(ResponseCode.EMPLOYMENT_RECRUITMENT_NO_TITLE);
		}
		if (dto.getContent() == null || dto.getContent().isBlank()) {
			throw new BusinessException(ResponseCode.EMPLOYMENT_RECRUITMENT_NO_CONTENT);
		}
		if (dto.getStartedAt() == null) {
			throw new BusinessException(ResponseCode.EMPLOYMENT_RECRUITMENT_NO_START_DATE);
		}
		if (dto.getEndedAt() == null) {
			throw new BusinessException(ResponseCode.EMPLOYMENT_RECRUITMENT_NO_END_DATE);
		}
		if (dto.getEndedAt().isBefore(dto.getStartedAt())) {
			throw new BusinessException(ResponseCode.EMPLOYMENT_RECRUITMENT_INVALID_DATE_ORDER);
		}
		if (dto.getIntroduceTemplateId() == 0) {
			throw new BusinessException(ResponseCode.EMPLOYMENT_RECRUITMENT_NO_INTRODUCE_TEMPLATE);
		}
		if (dto.getMemberId() == 0) {
			throw new BusinessException(ResponseCode.EMPLOYMENT_RECRUITMENT_NO_MEMBER_ID);
		}
	}
}
