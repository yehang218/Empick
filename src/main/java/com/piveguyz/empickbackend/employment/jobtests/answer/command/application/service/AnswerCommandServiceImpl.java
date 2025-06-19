package com.piveguyz.empickbackend.employment.jobtests.answer.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.CreateAnswerCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.CreateAnswerResponseDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.UpdateAnswerCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.aggregate.AnswerEntity;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.aggregate.GradingResultEntity;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.aggregate.enums.CorrectType;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.repository.AnswerRepository;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.mapper.AnswerMapper;

import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.repository.GradingResultRepository;
import com.piveguyz.empickbackend.employment.jobtests.grading.command.domain.aggregate.GradingCriteriaEntity;
import com.piveguyz.empickbackend.employment.jobtests.grading.command.domain.repository.GradingCriteriaRepository;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate.JobtestQuestionEntity;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.repository.ApplicationJobtestRepository;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.repository.JobtestQuestionRepository;
import com.piveguyz.empickbackend.employment.jobtests.question.command.domain.aggregate.QuestionEntity;
import com.piveguyz.empickbackend.facade.JobtestFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerCommandServiceImpl implements AnswerCommandService {

    private final AnswerRepository answerRepository;
    private final GradingCriteriaRepository gradingCriteriaRepository;
    private final JobtestQuestionRepository jobtestQuestionRepository;
    private final GradingResultRepository gradingResultRepository;
    private final ApplicationJobtestRepository applicationJobtestRepository;

    // 선택지 등록
    @Override
    public CreateAnswerResponseDTO createAnswer(CreateAnswerCommandDTO createAnswerCommandDTO) {
        int jobTestId = createAnswerCommandDTO.getApplicationJobTestId();
        int questionId = createAnswerCommandDTO.getQuestionId();

        Optional<AnswerEntity> existing = answerRepository.findByApplicationJobTestIdAndQuestionId(jobTestId, questionId);
        AnswerEntity entity;
        if (existing.isPresent()) {
            // 기존 답안
            entity = existing.get();

            // 답안이 동일한지 비교 (answer 필드가 String 기준이라면)
            boolean isSameAnswer = Objects.equals(entity.getContent(), createAnswerCommandDTO.getContent());
            int nextAttempt = isSameAnswer ? entity.getAttempt() : entity.getAttempt() + 1;
            entity.updateAnswerEntity(createAnswerCommandDTO, nextAttempt);
        } else {
            // 처음 시도라면
            entity = AnswerMapper.toEntity(createAnswerCommandDTO, 1);
        }

        AnswerEntity saved = answerRepository.save(entity);
        return AnswerMapper.toCreateDTO(saved);
    }

    @Override
    public UpdateAnswerCommandDTO updateAnswerCommandDTO(int id, UpdateAnswerCommandDTO updateAnswerCommandDTO) {
        // 있는지 확인
        AnswerEntity answer = answerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_JOBTEST_ANSWER));
        answer.updateAnswerEntity(updateAnswerCommandDTO);
        AnswerEntity updated = answerRepository.save(answer);
        return AnswerMapper.toUpdateDto(updated);
    }

    @Override
    public UpdateAnswerCommandDTO updateAnswerEntity(AnswerEntity answer) {
        answerRepository.save(answer);
        return AnswerMapper.toUpdateDto(answer);
    }

    // 선택지 삭제
    @Override
    public Integer deleteAnswer(int id) {
        AnswerEntity answer = answerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_JOBTEST_ANSWER));
        answerRepository.delete(answer);
        return answer.getId();
    }


    // 선택형, 단답형 채점하기
    @Override
    public void autoGrade(AnswerEntity answer, QuestionEntity question) {
        int maxScore = resolveScore(answer.getApplicationJobTestId(), question.getId());
        boolean isCorrect = question.getAnswer().trim().equalsIgnoreCase(answer.getContent().trim());

        answer.applyGradingResult(isCorrect ? CorrectType.CORRECT : CorrectType.WRONG, isCorrect ? maxScore : 0);
    }

    // 서술형 채점하기
    @Override
    public void descriptiveGrade(AnswerEntity answer, QuestionEntity question) {
        int maxScore = resolveScore(answer.getApplicationJobTestId(), question.getId());

        List<GradingCriteriaEntity> criteria = gradingCriteriaRepository.findByQuestionId(question.getId());
        List<GradingResultEntity> results = gradingResultRepository.findByAnswerId(answer.getId());

        double totalScore = 0.0;
        for (GradingResultEntity result : results) {
            // 채점자가 맞다고 채점한 항목만 total += scoreWeight * 문제 점수
            if ("Y".equalsIgnoreCase(result.getIsSatisfied())) {
                GradingCriteriaEntity criterion = criteria.stream()
                        .filter(c -> c.getId().equals(result.getQuestionGradingCriteriaId()))
                        .findFirst()
                        .orElse(null);
                if (criterion != null) {
                    totalScore += criterion.getScoreWeight() * maxScore;
                }
            }
        }

        if (totalScore == maxScore) answer.applyGradingResult(CorrectType.CORRECT, totalScore);
        else if (totalScore == 0) answer.applyGradingResult(CorrectType.WRONG, 0);
        else answer.applyGradingResult(CorrectType.PARTIAL, totalScore);
    }

    @Override
    public List<AnswerEntity> findByApplicationJobtestId(int applicationJobTestId) {
        return answerRepository.findByApplicationJobTestId(applicationJobTestId);
    }

    // 실무테스트에 등록된 문제 점수 가져오기
    public int resolveScore(int applicationJobtestId, int questionId) {
        // application_job_test_id로 job_test_id 조회
        int jobtestId = applicationJobtestRepository.findById(applicationJobtestId)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_APPLICATION_JOBTEST))
                .getJobTestId();

        // job_test_id와 question_id로 실무테스트별 문제에 할당된 점수 조회
        JobtestQuestionEntity jobtestQuestion = jobtestQuestionRepository.findByJobTestIdAndQuestionId(jobtestId, questionId)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_JOBTEST_QUESTION));
        return jobtestQuestion.getScore();
    }
}
