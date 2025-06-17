package com.piveguyz.empickbackend.employment.introduce.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceStandardItemCommandDTO;
import com.piveguyz.empickbackend.employment.introduce.command.application.mapper.IntroduceStandardItemMapper;
import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceStandardItemEntity;
import com.piveguyz.empickbackend.employment.introduce.command.domain.repository.IntroduceStandardItemRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IntroduceStandardItemCommandServiceImp implements IntroduceStandardItemCommandService {

    private final IntroduceStandardItemRepository introduceStandardItemRepository;

    @Override
    public IntroduceStandardItemCommandDTO create(IntroduceStandardItemCommandDTO dto) {
        // 1. ID 포함 여부 검사
        if (dto.getId() != null) {
            throw new BusinessException(ResponseCode.COMMAND_INVALID_ID_ON_CREATE);
        }
        // 2. content 중복 검사
        if (introduceStandardItemRepository.existsByContent(dto.getContent())) {
            throw new BusinessException(ResponseCode.DUPLICATE_STANDARD_ITEM_CONTENT_TITLE); // 또는 별도 코드 생성 가능
        }

        IntroduceStandardItemEntity entity = IntroduceStandardItemMapper.toEntity(dto);
        IntroduceStandardItemEntity savedEntity = introduceStandardItemRepository.save(entity);
        return IntroduceStandardItemMapper.toDTO(savedEntity);
    }

    @Override
    public int delete(int id) {
        IntroduceStandardItemEntity entity = introduceStandardItemRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.INTRODUCE_STANDARD_ITEM_NOT_FOUND));
        introduceStandardItemRepository.delete(entity);
        return id;
    }

}
