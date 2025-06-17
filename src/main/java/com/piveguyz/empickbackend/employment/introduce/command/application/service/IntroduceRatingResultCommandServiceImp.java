package com.piveguyz.empickbackend.employment.introduce.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceRatingResultCommandDTO;
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
    private final IntroduceRatingResultCommandMapper introduceRatingResultCommandMapper;

    @Override
    public IntroduceRatingResultCommandDTO create(IntroduceRatingResultCommandDTO dto) {
        // 중복 체크는 필요 시 여기에

        IntroduceRatingResultEntity entity = IntroduceRatingResultCommandMapper.toEntity(dto);
        entity.setUpdatedAt(LocalDateTime.now());

        IntroduceRatingResultEntity saved = introduceRatingResultRepository.save(entity);
        dto.setId(saved.getId()); // 직접 ID 설정
        return dto;
    }

    @Override
    public IntroduceRatingResultCommandDTO update(IntroduceRatingResultCommandDTO dto) {
        IntroduceRatingResultEntity entity = introduceRatingResultRepository.findById(dto.getId())
                .orElseThrow(() -> new BusinessException(ResponseCode.INTRODUCE_RATING_RESULT_NOT_FOUND));

        entity.setContent(dto.getContent());
        entity.setRatingScore(dto.getRatingScore());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy(dto.getUpdatedBy());

        IntroduceRatingResultEntity updated = introduceRatingResultRepository.save(entity);
        return IntroduceRatingResultCommandMapper.toDto(updated);
    }

    @Override
    public Integer delete(int id) {
        IntroduceRatingResultEntity entity = introduceRatingResultRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.INTRODUCE_RATING_RESULT_NOT_FOUND));

        introduceRatingResultRepository.delete(entity);
        return id;
    }

}
