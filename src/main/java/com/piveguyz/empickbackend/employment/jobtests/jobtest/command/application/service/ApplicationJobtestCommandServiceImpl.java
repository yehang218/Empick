package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.applicant.command.domain.repository.ApplicationRepository;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.CreateApplicationJobtestCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.UpdateApplicationJobtestCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.mapper.ApplicationJobtestMapper;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate.ApplicationJobtestEntity;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.repository.ApplicationJobtestRepository;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.repository.JobtestRepository;
import com.piveguyz.empickbackend.orgstructure.member.command.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationJobtestCommandServiceImpl implements ApplicationJobtestCommandService {

    private final MemberRepository memberRepository;
    private final JobtestRepository jobtestRepository;
    private final ApplicationJobtestRepository applicationJobtestRepository;
    private final ApplicationRepository applicationRepository;

    @Override
    public CreateApplicationJobtestCommandDTO createApplicaionJobtest(CreateApplicationJobtestCommandDTO createApplicationJobtestCommandDTO) {
//         🚩 없는 지원서인 경우
        if(!applicationRepository.existsById(createApplicationJobtestCommandDTO.getApplicationId())) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_APPLICATION_NOT_FOUND);
        }

        // 없는 실무테스트일 경우
        if(!jobtestRepository.existsById(createApplicationJobtestCommandDTO.getJobtestId())) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INVALID_JOBTEST);
        }

        // 유효하지 않은 입장 코드인경우
        validateEntryCode(createApplicationJobtestCommandDTO.getEntryCode());

        // 이미 할당된 지원서인경우
        if(applicationJobtestRepository.existsByApplicationId(createApplicationJobtestCommandDTO.getApplicationId())) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_APPLICATION_JOBTEST_ALREADY_EXISTS);
        }


        ApplicationJobtestEntity applicationJobtestEntity = ApplicationJobtestMapper.toEntity(createApplicationJobtestCommandDTO);
        ApplicationJobtestEntity saved = applicationJobtestRepository.save(applicationJobtestEntity);

        return ApplicationJobtestMapper.toCreateDto(saved);
    }

    @Override
    public UpdateApplicationJobtestCommandDTO updateApplicationJobtest(int id, UpdateApplicationJobtestCommandDTO updateApplicationJobtestCommandDTO) {
        // 존재하는 데이터인지 확인
        ApplicationJobtestEntity applicationJobtest = applicationJobtestRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_APPLICATION_JOBTEST));

        // 유효하지 않은 입장 코드인 경우
        validateEntryCode(updateApplicationJobtestCommandDTO.getEntryCode());

        // 채점자를 수정할건데 member에 없는 경우
        validateMemberExists(updateApplicationJobtestCommandDTO.getGradingMemberId());

        // 평가자를 수정할건데 member에 없는 경우
        validateMemberExists(updateApplicationJobtestCommandDTO.getEvaluationMemberId());

        applicationJobtest.updateApplicationJobtestEntity(updateApplicationJobtestCommandDTO);
        ApplicationJobtestEntity updatedEntity = applicationJobtestRepository.save(applicationJobtest);

        return ApplicationJobtestMapper.toUpdateDto(updatedEntity);
    }

    @Override
    public int deleteApplicationJobtest(int id) {
        ApplicationJobtestEntity applicationJobtest = applicationJobtestRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_APPLICATION_JOBTEST));
        applicationJobtestRepository.delete(applicationJobtest);
        return applicationJobtest.getId();
    }

    // 채점 후에 점수, 채점 상태 자동 업데이트
    @Override
    public void updateGradingStatusAndScore(int applicationJobTestId, double totalScore) {
        ApplicationJobtestEntity applicationJobtest = applicationJobtestRepository.findById(applicationJobTestId)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_APPLICATION_JOBTEST));

        applicationJobtest.completeGrading(totalScore);
        applicationJobtestRepository.save(applicationJobtest);
    }


    private void validateMemberExists(Integer memberId) {
        if (memberId != null && !memberRepository.existsById(memberId)) {
            throw new BusinessException(ResponseCode.MEMBER_ID_INVALID);
        }
    }

    private void validateEntryCode(String entryCode) {
        if (entryCode != null) {
//            // 5자리 숫자가 아닌 경우
//            if (!entryCode.matches("^\\d{5}$")) {
//                throw new BusinessException(ResponseCode.EMPLOYMENT_INVALID_ENTRY_CODE);
//            }

            // 중복된 경우
            if (applicationJobtestRepository.existsByEntryCode(entryCode)) {
                throw new BusinessException(ResponseCode.EMPLOYMENT_ENTRY_CODE_DUPLICATE);
            }
        }
    }

}
