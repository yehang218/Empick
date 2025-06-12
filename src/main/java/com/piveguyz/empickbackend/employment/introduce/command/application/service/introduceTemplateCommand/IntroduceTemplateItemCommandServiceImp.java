package com.piveguyz.empickbackend.employment.introduce.command.application.service.introduceTemplateCommand;

import com.piveguyz.empickbackend.employment.introduce.command.application.dto.introduceTemplate.IntroduceTemplateItemCommandDTO;
import com.piveguyz.empickbackend.employment.introduce.command.application.mapper.IntroduceTemplateItemMapper;
import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.introduceTemplateCommand.IntroduceTemplateItemEntity;
import com.piveguyz.empickbackend.employment.introduce.command.domain.repository.introduceTemplateCommand.IntroduceTemplateItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IntroduceTemplateItemCommandServiceImp implements IntroduceTemplateItemCommandService {

    private final IntroduceTemplateItemRepository introduceTemplateItemRepository;

        @Override
        public IntroduceTemplateItemCommandDTO create(IntroduceTemplateItemCommandDTO dto) {
            IntroduceTemplateItemEntity entity = IntroduceTemplateItemMapper.toEntity(dto);
            IntroduceTemplateItemEntity savedEntity = introduceTemplateItemRepository.save(entity);
            return IntroduceTemplateItemMapper.toDTO(savedEntity);
        }
    }
