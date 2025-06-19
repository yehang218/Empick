package com.piveguyz.empickbackend.employment.introduce.command.application.service;


import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceTemplateItemResponseCommandDTO;
import com.piveguyz.empickbackend.employment.introduce.command.application.mapper.IntroduceTemplateItemResponseCommandMapper;
import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceTemplateItemResponseEntity;
import com.piveguyz.empickbackend.employment.introduce.command.domain.repository.IntroduceTemplateItemResponseCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IntroduceTemplateItemResponseCommandServiceImp implements IntroduceTemplateItemResponseCommandService {

    private final IntroduceTemplateItemResponseCommandRepository introduceTemplateItemResponseCommandRepository;
    private final IntroduceTemplateItemResponseCommandMapper introduceTemplateItemResponseCommandMapper;

    @Override
    public IntroduceTemplateItemResponseCommandDTO create(IntroduceTemplateItemResponseCommandDTO dto) {
        IntroduceTemplateItemResponseEntity entity = introduceTemplateItemResponseCommandMapper.toEntity(dto);
        IntroduceTemplateItemResponseEntity saved = introduceTemplateItemResponseCommandRepository.save(entity);
        return introduceTemplateItemResponseCommandMapper.toDTO(saved);

    }
}
