package com.piveguyz.empickbackend.employment.applicant.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.applicant.command.application.dto.ApplicantCommandDTO;
import com.piveguyz.empickbackend.employment.applicant.command.application.mapper.ApplicantCommandMapper;
import com.piveguyz.empickbackend.employment.applicant.command.domain.aggregate.ApplicantEntity;
import com.piveguyz.empickbackend.employment.applicant.command.domain.repository.ApplicantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicantCommandServiceImp implements ApplicantCommandService {

    private final ApplicantRepository applicantRepository;

    @Override
    public ApplicantCommandDTO createApplicant(ApplicantCommandDTO dto) {

        if (applicantRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_APPLICANT_DUPLICATE_EMAIL);
        }

        if (applicantRepository.existsByPhone(dto.getPhone())) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_APPLICANT_DUPLICATE_PHONE);
        }

        ApplicantEntity entity = ApplicantCommandMapper.toEntity(dto);
        ApplicantEntity saved = applicantRepository.save(entity);
        return ApplicantCommandMapper.toDto(saved);
    }

//    @Override
//    public ApplicantCommandDTO updateApplicant(int id, ApplicantCommandDTO dto) {
//        ApplicantEntity entity = axpplicantRepository.findById(id)
//                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_APPLICANT));
//        entity.updateFromDTO(dto);
//        ApplicantEntity saved = applicantRepository.save(entity);
//        return ApplicantCommandMapper.toDto(saved);
//    }
//
//    @Override
//    public int deleteApplicant(int id) {
//        ApplicantEntity entity = applicantRepository.findById(id)
//                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_APPLICANT));
//        applicantRepository.delete(entity);
//        return id;
//    }
}
