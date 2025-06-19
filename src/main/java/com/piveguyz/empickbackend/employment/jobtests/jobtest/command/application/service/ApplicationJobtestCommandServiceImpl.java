package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.common.util.RandomCodeUtil;
import com.piveguyz.empickbackend.employment.applicant.command.domain.repository.ApplicationRepository;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.CreateApplicationJobtestCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.JobtestEntryRequestDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.UpdateApplicationJobtestCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.mapper.ApplicationJobtestMapper;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate.ApplicationJobtestEntity;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate.JobtestEntity;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.repository.ApplicationJobtestRepository;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.repository.JobtestRepository;
import com.piveguyz.empickbackend.orgstructure.member.command.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationJobtestCommandServiceImpl implements ApplicationJobtestCommandService {

    private final MemberRepository memberRepository;
    private final JobtestRepository jobtestRepository;
    private final ApplicationJobtestRepository applicationJobtestRepository;
    private final ApplicationRepository applicationRepository;

    @Override
    public CreateApplicationJobtestCommandDTO createApplicaionJobtest(CreateApplicationJobtestCommandDTO createApplicationJobtestCommandDTO) {
//      // 없는 지원서인 경우
        if(!applicationRepository.existsById(createApplicationJobtestCommandDTO.getApplicationId())) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_APPLICATION_NOT_FOUND);
        }

        // 없는 실무테스트일 경우
        if(!jobtestRepository.existsById(createApplicationJobtestCommandDTO.getJobtestId())) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INVALID_JOBTEST);
        }

        // 이미 할당된 지원서인경우
        if(applicationJobtestRepository.existsByApplicationId(createApplicationJobtestCommandDTO.getApplicationId())) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_APPLICATION_JOBTEST_ALREADY_EXISTS);
        }

        // 중복되지 않은 입장 코드 생성(jobtest마다)
        String entryCode = generateUniqueEntryCode(createApplicationJobtestCommandDTO.getJobtestId());

        ApplicationJobtestEntity applicationJobtestEntity = ApplicationJobtestMapper.toEntity(createApplicationJobtestCommandDTO, entryCode);
        ApplicationJobtestEntity saved = applicationJobtestRepository.save(applicationJobtestEntity);

        return ApplicationJobtestMapper.toCreateDto(saved);
    }

    @Override
    public UpdateApplicationJobtestCommandDTO updateApplicationJobtest(int id, UpdateApplicationJobtestCommandDTO updateApplicationJobtestCommandDTO) {
        // 존재하는 데이터인지 확인
        ApplicationJobtestEntity applicationJobtest = applicationJobtestRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_APPLICATION_JOBTEST));

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

    @Override
    public void finishExam(int applicationJobTestId) {
        ApplicationJobtestEntity applicationJobtest = applicationJobtestRepository.findById(applicationJobTestId)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_APPLICATION_JOBTEST));
        applicationJobtest.destroyEntryCode();
        applicationJobtest.updateSubittedAt();
    }


    private void validateMemberExists(Integer memberId) {
        if (memberId != null && !memberRepository.existsById(memberId)) {
            throw new BusinessException(ResponseCode.MEMBER_ID_INVALID);
        }
    }

    // 랜덤 입장 코드 생성
    private String generateUniqueEntryCode(int jobtestId) {
        String entryCode;
        int retry = 0;
        do {
            entryCode = RandomCodeUtil.generateCode(6); // 6자리의 알파벳/숫자 조합
            retry++;
            if (retry > 10) {
                throw new BusinessException(ResponseCode.EMPLOYMENT_ENTRY_CODE_GENERATION_FAILED);
            }
        } while (applicationJobtestRepository.existsByJobTestIdAndEntryCode(jobtestId, entryCode));

        return entryCode;
    }

}
