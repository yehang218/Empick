package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.CreateJobtestCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.UpdateJobtestCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.mapper.JobtestMapper;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate.JobtestEntity;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.repository.JobtestRepository;
import com.piveguyz.empickbackend.orgstructure.member.command.domain.repository.MemberRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class JobtestCommandServiceImpl implements JobtestCommandService {
    private final JobtestRepository jobtestRepository;
    private final MemberRepository memberRepository;

    public JobtestCommandServiceImpl(JobtestRepository jobtestRepository,
                                     MemberRepository memberRepository) {
        this.jobtestRepository = jobtestRepository;
        this.memberRepository = memberRepository;
    }


    // 실무테스트 등록
    @Override
    public int createJobtest(CreateJobtestCommandDTO createJobtestCommandDTO) {
        // 같은 이름이 이미 존재하는 경우
        if (jobtestRepository.existsByTitle(createJobtestCommandDTO.getTitle())) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_JOBTEST_DUPLICATE);
        }

        // 작성자가 없는 사원인 경우
        if (!memberRepository.existsById(createJobtestCommandDTO.getCreatedMemberId())) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INVALID_MEMBER);
        }

        JobtestEntity jobtestEntity = JobtestMapper.toEntity(createJobtestCommandDTO);
        JobtestEntity saved = jobtestRepository.save(jobtestEntity);

        return saved.getId();
    }

    // 실무테스트 수정
    @Override
    public UpdateJobtestCommandDTO updateJobtest(int id, UpdateJobtestCommandDTO updateJobtestCommandDTO) {
        // 실무테스트 있는지 확인
        JobtestEntity jobtest = jobtestRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_JOBTEST));

        // 수정자가 만약 없는 회원이라면
        if (!memberRepository.existsById(updateJobtestCommandDTO.getUpdatedMemberId())) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INVALID_UPDATED_MEMBER);
        }

        jobtest.updateJobtestEntity(updateJobtestCommandDTO);
        JobtestEntity updatedEntity = jobtestRepository.save(jobtest);
        return JobtestMapper.toUpdateDto(updatedEntity);
    }

    // 실무테스트 삭제
    @Override
    public Integer deleteJobtest(int id) {
        JobtestEntity jobtest = jobtestRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_JOBTEST));

        try {
            jobtestRepository.delete(jobtest);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_JOBTEST_DELETE_CONFLICT);
        }

        return jobtest.getId();
    }

    @Override
    public Optional<JobtestEntity> findById(int jobtestId) {
        return jobtestRepository.findById(jobtestId);
    }

    @Override
    public void checkJobtestTime(int jobtestId) {
        LocalDateTime now = LocalDateTime.now();
        JobtestEntity jobtest = jobtestRepository.findById(jobtestId)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_JOBTEST));
        if(now.isBefore(jobtest.getStartedAt()) || now.isAfter(jobtest.getEndedAt())) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INVALID_TIME);
        }
    }
}
