package com.piveguyz.empickbackend.employment.introduce.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceTemplateCommandDTO;
import com.piveguyz.empickbackend.employment.introduce.command.application.mapper.IntroduceTemplateCommandMapper;
import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceTemplateEntity;
import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceTemplateItemEntity;
import com.piveguyz.empickbackend.employment.introduce.command.domain.repository.IntroduceTemplateRepository;
import com.piveguyz.empickbackend.employment.introduce.command.domain.repository.IntroduceTemplateItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IntroduceTemplateCommandServiceImp implements IntroduceTemplateCommandService {

    private final IntroduceTemplateRepository introduceTemplateRepository;
    private final IntroduceTemplateItemRepository introduceTemplateItemRepository;

    @Override
    @Transactional
    public IntroduceTemplateCommandDTO create(IntroduceTemplateCommandDTO dto) {
        // 1. ID 포함 여부 검사
        if (dto.getId() != null) {
            throw new BusinessException(ResponseCode.COMMAND_INVALID_ID_ON_CREATE);
        }

        // 2. 중복 title 검사
        if (introduceTemplateRepository.existsByTitle(dto.getTitle())) {
            throw new BusinessException(ResponseCode.DUPLICATE_TEMPLATE_TITLE);
        }

        // 3. 템플릿 저장
        IntroduceTemplateEntity entity = IntroduceTemplateCommandMapper.toEntity(dto);
        IntroduceTemplateEntity savedEntity = introduceTemplateRepository.save(entity);

        // 4. 선택한 항목들 introduce_template_id 업데이트
        if (dto.getItemIds() != null && !dto.getItemIds().isEmpty()) {
            for (Integer itemId : dto.getItemIds()) {
                IntroduceTemplateItemEntity item = introduceTemplateItemRepository.findById(itemId)
                        .orElseThrow(() -> new BusinessException(ResponseCode.INTRODUCE_TEMPLATE_ITEM_NOT_FOUND));
                item.setIntroduceTemplateId(savedEntity.getId());
                introduceTemplateItemRepository.save(item);
            }
        }

        // 5. DTO 반환 (itemIds 포함)
        return IntroduceTemplateCommandMapper.toDTO(savedEntity, dto.getItemIds());
    }

    @Override
    public int delete(int id) {
        IntroduceTemplateEntity entity = introduceTemplateRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.INTRODUCE_TEMPLATE_NOT_FOUND));
        introduceTemplateRepository.delete(entity);
        return id;
    }
}