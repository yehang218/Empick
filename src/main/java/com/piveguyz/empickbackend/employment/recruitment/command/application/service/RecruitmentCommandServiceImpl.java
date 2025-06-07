package com.piveguyz.empickbackend.employment.recruitment.command.application.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.applicationItem.command.application.dto.ApplicationItemCommandDTO;
import com.piveguyz.empickbackend.employment.applicationItem.command.domain.aggregate.ApplicationItem;
import com.piveguyz.empickbackend.employment.applicationItem.command.domain.aggregate.ApplicationItemCategory;
import com.piveguyz.empickbackend.employment.applicationItem.command.domain.repository.ApplicationItemCategoryRepository;
import com.piveguyz.empickbackend.employment.applicationItem.command.domain.repository.ApplicationItemRepository;
import com.piveguyz.empickbackend.employment.applicationItem.query.dto.ApplicationItemCreateDTO;
import com.piveguyz.empickbackend.employment.recruitment.command.application.dto.RecruitmentCommandDTO;
import com.piveguyz.empickbackend.employment.recruitment.command.domain.aggregate.Recruitment;
import com.piveguyz.empickbackend.employment.recruitment.command.domain.enums.RecruitmentStatus;
import com.piveguyz.empickbackend.employment.recruitment.command.domain.repository.RecruitmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RecruitmentCommandServiceImpl implements RecruitmentCommandService {
	private final RecruitmentRepository recruitmentRepository;
	private final ApplicationItemRepository applicationItemRepository;
	private final ApplicationItemCategoryRepository applicationItemCategoryRepository;

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
			.status(RecruitmentStatus.WAITING) // 대기 상태
			.memberId(dto.getMemberId())
			.recruitmentTemplateId(dto.getRecruitmentTemplateId())
			.introduceTemplateId(dto.getIntroduceTemplateId())
			.recruitmentRequestId(dto.getRecruitmentRequestId())
			.build();

		Recruitment saved = recruitmentRepository.save(recruitment);

		// 지원서 항목 저장
		for (ApplicationItemCreateDTO itemDTO : dto.getApplicationItems()) {
			ApplicationItemCategory category = applicationItemCategoryRepository.findById(itemDTO.getApplicationItemCategoryId())
				.orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_APPLICATION_ITEM_CATEGORY_NOT_FOUND));

			ApplicationItem item = ApplicationItem.builder()
				.recruitment(saved)
				.category(category)
				.isRequiredYn(itemDTO.isRequired() ? "Y" : "N")
				.build();

			applicationItemRepository.save(item);
		}
	}

	@Override
	public void updateRecruitment(int id, RecruitmentCommandDTO dto) {
		Recruitment recruitment = recruitmentRepository.findById(id)
			.orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_RECRUITMENT_NOT_FOUND));

		if (recruitment.getStatus() == RecruitmentStatus.PUBLISHED) {
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

	@Override
	public void updateStatus(int id, int statusCode) {
		Recruitment recruitment = recruitmentRepository.findById(id)
			.orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_RECRUITMENT_NOT_FOUND));

		RecruitmentStatus currentStatus = recruitment.getStatus();
		RecruitmentStatus nextStatus = RecruitmentStatus.fromCode(statusCode);

		boolean valid =
			(currentStatus == RecruitmentStatus.WAITING && nextStatus == RecruitmentStatus.APPROVED) ||
				(currentStatus == RecruitmentStatus.APPROVED && nextStatus == RecruitmentStatus.PUBLISHED) ||
				(currentStatus == RecruitmentStatus.PUBLISHED && nextStatus == RecruitmentStatus.CLOSED);

		if (!valid) {
			throw new BusinessException(ResponseCode.EMPLOYMENT_RECRUITMENT_INVALID_STATUS_TRANSITION);
		}

		recruitment.setStatus(nextStatus);
	}

	@Override
	public void deleteRecruitment(int id) {
		Recruitment recruitment = recruitmentRepository.findById(id)
			.orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_RECRUITMENT_NOT_FOUND));

		if (recruitment.getDeletedAt() != null) {
			throw new BusinessException(ResponseCode.EMPLOYMENT_RECRUITMENT_ALREADY_DELETED);
		}

		recruitment.setDeletedAt(LocalDateTime.now());
	}
}
