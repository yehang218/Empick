package com.piveguyz.empickbackend.employment.recruitmentTemplateCopy.command.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.recruitment.command.domain.aggregate.Recruitment;
import com.piveguyz.empickbackend.employment.recruitment.command.domain.repository.RecruitmentRepository;
import com.piveguyz.empickbackend.employment.recruitmentTemplate.command.domain.aggregate.RecruitmentTemplateItem;
import com.piveguyz.empickbackend.employment.recruitmentTemplate.command.domain.repository.RecruitmentTemplateItemRepository;
import com.piveguyz.empickbackend.employment.recruitmentTemplateCopy.command.domain.aggregate.RecruitmentTemplateCopy;
import com.piveguyz.empickbackend.employment.recruitmentTemplateCopy.command.domain.repository.RecruitmentTemplateCopyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RecruitmentTemplateCopyCommandServiceImpl implements RecruitmentTemplateCopyCommandService {
	private final RecruitmentTemplateItemRepository recruitmentTemplateItemRepository;
	private final RecruitmentRepository recruitmentRepository;
	private final RecruitmentTemplateCopyRepository recruitmentTemplateCopyRepository;

	@Override
	public void copyTemplateItemsToRecruitment(int recruitmentId, int templateId) {
		Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
			.orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_RECRUITMENT_NOT_FOUND));

		List<RecruitmentTemplateItem> items = recruitmentTemplateItemRepository.findByRecruitmentTemplateId(templateId);

		for (RecruitmentTemplateItem item : items) {
			RecruitmentTemplateCopy copy = RecruitmentTemplateCopy.builder()
				.recruitment(recruitment)
				.recruitmentTemplateItem(item)
				.title(item.getItemTitle())
				.content(item.getDefaultContent())
				.displayOrder(item.getDisplayOrder())
				.build();

			recruitmentTemplateCopyRepository.save(copy);
		}
	}
}
