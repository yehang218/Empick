package com.piveguyz.empickbackend.employment.introduce.command.application.service.introduceTemplateCommand;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
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

            // 1. ID 값이 들어왔는지 확인
            if (dto.getId() != null) {
                throw new BusinessException(ResponseCode.COMMAND_INVALID_ID_ON_CREATE);
            }

            // 2. title 중복 확인
            if (introduceTemplateItemRepository.existsByTitle(dto.getTitle())) {
                throw new BusinessException(ResponseCode.DUPLICATE_TEMPLATE_ITEM_TITLE);
            }


            IntroduceTemplateItemEntity entity = IntroduceTemplateItemMapper.toEntity(dto);
            IntroduceTemplateItemEntity savedEntity = introduceTemplateItemRepository.save(entity);
            return IntroduceTemplateItemMapper.toDTO(savedEntity);
        }

        @Override
        public int delete(int id) {
             IntroduceTemplateItemEntity entity = introduceTemplateItemRepository.findById(id)
                  .orElseThrow(() -> new BusinessException(ResponseCode.INTRODUCE_TEMPLATE_ITEM_NOT_FOUND));
             introduceTemplateItemRepository.delete(entity);
            return id;
    }

}
