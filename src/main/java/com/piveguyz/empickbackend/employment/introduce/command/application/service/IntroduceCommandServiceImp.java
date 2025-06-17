package com.piveguyz.empickbackend.employment.introduce.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceCommandDTO;
import com.piveguyz.empickbackend.employment.introduce.command.application.mapper.IntroduceMapper;
import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceEntity;
import com.piveguyz.empickbackend.employment.introduce.command.domain.repository.IntroduceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IntroduceCommandServiceImp implements IntroduceCommandService {

    private final IntroduceRepository introduceRepository;

    @Override
    public IntroduceCommandDTO create(IntroduceCommandDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException(ResponseCode.COMMAND_INVALID_ID_ON_CREATE);
        }

        boolean isDuplicate = introduceRepository.existsByApplicantIdAndIntroduceTemplateId(
                dto.getApplicantId(), dto.getIntroduceTemplateId()
        );
        if (isDuplicate) {
            throw new BusinessException(ResponseCode.DUPLICATE_INTRODUCE_FOR_TEMPLATE);
        }

        IntroduceEntity entity = IntroduceMapper.toEntity(dto);
        IntroduceEntity savedEntity = introduceRepository.save(entity);
        return IntroduceMapper.toDTO(savedEntity);
    }
}
