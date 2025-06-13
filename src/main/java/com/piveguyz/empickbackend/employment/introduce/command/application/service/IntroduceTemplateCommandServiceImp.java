package com.piveguyz.empickbackend.employment.introduce.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceTemplateCommandDTO;
import com.piveguyz.empickbackend.employment.introduce.command.application.mapper.IntroduceTemplateCommandMapper;
import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceTemplateEntity;
import com.piveguyz.empickbackend.employment.introduce.command.domain.repository.IntroduceTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IntroduceTemplateCommandServiceImp implements IntroduceTemplateCommandService {

    private final IntroduceTemplateRepository introduceTemplateRepository;

    @Override
    public IntroduceTemplateCommandDTO create(IntroduceTemplateCommandDTO dto) {

        // 1. ID 포함 여부 검사
        if (dto.getId() != null) {
            throw new BusinessException(ResponseCode.COMMAND_INVALID_ID_ON_CREATE);
        }

        // 2. 중복 title 검사
        if (introduceTemplateRepository.existsByTitle(dto.getTitle())) {
            throw new BusinessException(ResponseCode.DUPLICATE_TEMPLATE_TITLE);
        }

        IntroduceTemplateEntity entity = IntroduceTemplateCommandMapper.toEntity(dto);
        IntroduceTemplateEntity savedEntity = introduceTemplateRepository.save(entity);
            return IntroduceTemplateCommandMapper.toDTO(savedEntity);
        }
        @Override
        public int delete(int id) {
            IntroduceTemplateEntity entity = introduceTemplateRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(ResponseCode.INTRODUCE_TEMPLATE_NOT_FOUND));
            introduceTemplateRepository.delete(entity);
            return id;
        }

    }
