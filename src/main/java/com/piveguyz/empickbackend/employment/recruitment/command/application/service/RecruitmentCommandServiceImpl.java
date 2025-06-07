package com.piveguyz.empickbackend.employment.recruitment.command.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

		// 게시된 공고는 수정 불가
		if (recruitment.getStatus() == RecruitmentStatus.PUBLISHED) {
			throw new BusinessException(ResponseCode.EMPLOYMENT_RECRUITMENT_CANNOT_MODIFY_PUBLISHED);
		}

		validate(dto);
		recruitment.update(dto);

		List<ApplicationItem> existingItems = applicationItemRepository.findByRecruitmentId(recruitment.getId());
		Map<Integer, ApplicationItem> existingMap = existingItems.stream()
			.collect(Collectors.toMap(item -> item.getCategory().getId(), item -> item));

		Set<Integer> incomingCategoryIds = dto.getApplicationItems().stream()
			.map(ApplicationItemCreateDTO::getApplicationItemCategoryId)
			.collect(Collectors.toSet());

		for (ApplicationItem existing : existingItems) {
			if (!incomingCategoryIds.contains(existing.getCategory().getId())) {
				applicationItemRepository.delete(existing);
			}
		}

		for (ApplicationItemCreateDTO itemDTO : dto.getApplicationItems()) {
			ApplicationItem existing = existingMap.get(itemDTO.getApplicationItemCategoryId());
			if (existing != null) {
				existing.setIsRequiredYn(itemDTO.isRequired() ? "Y" : "N");
			} else {
				ApplicationItemCategory category = applicationItemCategoryRepository.findById(itemDTO.getApplicationItemCategoryId())
					.orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_APPLICATION_ITEM_CATEGORY_NOT_FOUND));
				ApplicationItem newItem = ApplicationItem.builder()
					.recruitment(recruitment)
					.category(category)
					.isRequiredYn(itemDTO.isRequired() ? "Y" : "N")
					.build();
				applicationItemRepository.save(newItem);
			}
		}
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
