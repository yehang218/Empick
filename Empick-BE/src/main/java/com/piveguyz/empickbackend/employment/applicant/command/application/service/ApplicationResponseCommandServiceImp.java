package com.piveguyz.empickbackend.employment.applicant.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.applicant.command.application.dto.ApplicationResponseCommandDTO;
import com.piveguyz.empickbackend.employment.applicant.command.application.mapper.ApplicationResponseCommandMapper;
import com.piveguyz.empickbackend.employment.applicant.command.domain.aggregate.ApplicationResponseEntity;
import com.piveguyz.empickbackend.employment.applicant.command.domain.repository.ApplicationResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationResponseCommandServiceImp implements ApplicationResponseCommandService {

    private final ApplicationResponseRepository applicationResponseRepository;


    @Override
    public ApplicationResponseCommandDTO saveResponse(ApplicationResponseCommandDTO dto) {

        // 예외 1: 응답 내용이 비어 있으면 예외 발생
        if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            throw new BusinessException(ResponseCode.APPLICATION_RESPONSE_EMPTY_CONTENT);
        }

        // 예외 2: 동일한 application_id와 application_item_id의 조합이 이미 존재하면 예외 발생
        if (applicationResponseRepository.existsByApplicationIdAndApplicationItemId(
                dto.getApplicationId(), dto.getApplicationItemId()
        )) {
            throw new BusinessException(ResponseCode.APPLICATION_RESPONSE_DUPLICATE);
        }

        // 엔티티로 변환 후 저장
        ApplicationResponseEntity entity = ApplicationResponseCommandMapper.toEntity(dto);
        ApplicationResponseEntity saved = applicationResponseRepository.save(entity);

        // 저장된 엔티티를 DTO로 변환하여 반환
        return ApplicationResponseCommandMapper.toDto(saved);
    }
}

