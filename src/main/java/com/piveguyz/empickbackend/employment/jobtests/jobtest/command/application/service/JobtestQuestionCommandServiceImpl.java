package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.CreateJobtestQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.CreateJobtestQuestionResponseDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.UpdateJobtestQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.mapper.JobtestQuestionMapper;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate.JobtestQuestionEntity;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.repository.JobtestQuestionRepository;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.repository.JobtestRepository;
import com.piveguyz.empickbackend.employment.jobtests.question.command.domain.repository.QuestionRepository;
import org.springframework.stereotype.Service;

@Service
public class JobtestQuestionCommandServiceImpl implements JobtestQuestionCommandService {

    private final JobtestQuestionRepository jobtestQuestionRepository;
    private final JobtestRepository jobtestRepository;
    private final QuestionRepository questionRepository;

    JobtestQuestionCommandServiceImpl(JobtestQuestionRepository jobtestQuestionRepository,
                                      JobtestRepository jobtestRepository,
                                      QuestionRepository questionRepository) {
        this.jobtestQuestionRepository = jobtestQuestionRepository;
        this.jobtestRepository = jobtestRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public CreateJobtestQuestionResponseDTO createJobtestQuestion(CreateJobtestQuestionCommandDTO createJobtestQuestionCommandDTO) {
        int jobtestId = createJobtestQuestionCommandDTO.getJobTestId();
        int questionId = createJobtestQuestionCommandDTO.getQuestionId();

        // 실무테스트가 없는 경우
        if(!jobtestRepository.existsById(jobtestId)) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INVALID_JOBTEST);
        }

        // 문제가 없는 경우
        if(!questionRepository.existsById(questionId)) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INVALID_QUESTION);
        }

        // 이미 있는 경우
        if (jobtestQuestionRepository.existsByJobTestIdAndQuestionId(jobtestId, questionId)) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_JOBTEST_QUESTION_DUPLICATE);
        }

        // 문제 순서 자동 할당
        Integer maxOptionNumber = jobtestQuestionRepository.findMaxOptionNumberByJobTestId(jobtestId);
        int nextOptionNumber = maxOptionNumber != null ? maxOptionNumber + 1 : 1;

        JobtestQuestionEntity jobtestQuestionEntity = JobtestQuestionMapper.toEntity(createJobtestQuestionCommandDTO, nextOptionNumber);
        JobtestQuestionEntity saved = jobtestQuestionRepository.save(jobtestQuestionEntity);

        return JobtestQuestionMapper.toCreateDto(saved);
    }

    @Override
    public UpdateJobtestQuestionCommandDTO updateJobtestQuestion(int id, UpdateJobtestQuestionCommandDTO updateJobtestQuestionCommandDTO) {
        // 있는 데이터인지 확인
        JobtestQuestionEntity jobtestQuestion = jobtestQuestionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_JOBTEST_QUESTION));

        jobtestQuestion.updateJobtestQuestionEntity(updateJobtestQuestionCommandDTO);
        JobtestQuestionEntity updatedEntity = jobtestQuestionRepository.save(jobtestQuestion);

        return JobtestQuestionMapper.toUpdateDto(updatedEntity);
    }

    @Override
    public int deleteJobtestQuestion(int id) {
        JobtestQuestionEntity jobtestQuestion = jobtestQuestionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_JOBTEST_QUESTION));
        jobtestQuestionRepository.delete(jobtestQuestion);
        return jobtestQuestion.getId();
    }
}
