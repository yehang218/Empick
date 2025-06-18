package com.piveguyz.empickbackend.employment.introduce.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceRatingResultCreateCommandDTO;
import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceRatingResultUpdateCommandDTO;
import com.piveguyz.empickbackend.employment.introduce.command.application.mapper.IntroduceRatingResultCommandMapper;
import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceRatingResultEntity;
import com.piveguyz.empickbackend.employment.introduce.command.domain.repository.IntroduceRatingResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class IntroduceRatingResultCommandServiceImp implements IntroduceRatingResultCommandService {

    private final IntroduceRatingResultRepository introduceRatingResultRepository;

    @Override
    public IntroduceRatingResultCreateCommandDTO create(IntroduceRatingResultCreateCommandDTO dto) {
        IntroduceRatingResultEntity entity = IntroduceRatingResultCommandMapper.toEntity(dto);
        entity.setUpdatedAt(LocalDateTime.now());  // 수정 시각은 등록 시에도 초기화

        IntroduceRatingResultEntity saved = introduceRatingResultRepository.save(entity);
        return IntroduceRatingResultCommandMapper.toCreateDto(saved);
    }

    @Override
    public IntroduceRatingResultUpdateCommandDTO update(int id, IntroduceRatingResultUpdateCommandDTO dto) {
        IntroduceRatingResultEntity entity = introduceRatingResultRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.INTRODUCE_RATING_RESULT_NOT_FOUND));

        entity.updateIntroduceRatingResultEntity(dto);
        introduceRatingResultRepository.save(entity);

        return IntroduceRatingResultCommandMapper.toUpdateDto(entity);
    }
    @Override
    public Integer delete(int id) {
        IntroduceRatingResultEntity entity = introduceRatingResultRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.INTRODUCE_RATING_RESULT_NOT_FOUND));

        introduceRatingResultRepository.delete(entity);
        return id;
    }

}
