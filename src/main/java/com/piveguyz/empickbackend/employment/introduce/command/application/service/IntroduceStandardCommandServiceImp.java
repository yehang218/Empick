package com.piveguyz.empickbackend.employment.introduce.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceStandardCommandDTO;
import com.piveguyz.empickbackend.employment.introduce.command.application.mapper.IntroduceStandardCommandMapper;
import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceStandardEntity;
import com.piveguyz.empickbackend.employment.introduce.command.domain.repository.IntroduceStandardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IntroduceStandardCommandServiceImp implements IntroduceStandardCommandService {

    private final IntroduceStandardRepository introduceStandardRepository;

    @Override
    public IntroduceStandardCommandDTO create(IntroduceStandardCommandDTO dto) {
        // 1. ID 포함되면 예외 처리
        if (dto.getId() != null) {
            throw new BusinessException(ResponseCode.COMMAND_INVALID_ID_ON_CREATE);
        }

        // 2. 중복 content 검사
        if (introduceStandardRepository.existsByContent(dto.getContent())) {
            throw new BusinessException(ResponseCode.DUPLICATE_STANDARD_CONTENT);
        }

        IntroduceStandardEntity entity = IntroduceStandardCommandMapper.toEntity(dto);
        IntroduceStandardEntity savedEntity = introduceStandardRepository.save(entity);
        return IntroduceStandardCommandMapper.toDTO(savedEntity);
    }

    @Override
    public int delete(int id) {
        IntroduceStandardEntity entity = introduceStandardRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.INTRODUCE_STANDARD_NOT_FOUND));
        introduceStandardRepository.delete(entity);
        return id;
    }

}
