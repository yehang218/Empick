package com.piveguyz.empickbackend.facade;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.UpdateAnswerCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.service.AnswerCommandService;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.aggregate.AnswerEntity;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.*;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.mapper.JobtestMapper;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.service.ApplicationJobtestCommandService;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.service.JobtestCommandService;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.service.JobtestQuestionCommandService;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate.ApplicationJobtestEntity;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate.JobtestEntity;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.JobtestExamQueryDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.service.ApplicationJobtestQueryService;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.service.JobtestQueryService;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.service.QuestionCommandService;
import com.piveguyz.empickbackend.employment.jobtests.question.command.domain.aggregate.QuestionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JobtestFacade {
    private final JobtestCommandService jobtestCommandService;
    private final JobtestQueryService jobtestQueryService;

    private final JobtestQuestionCommandService jobtestQuestionCommandService;

    private final AnswerCommandService answerCommandService;
    private final QuestionCommandService questionCommandService;
    private final ApplicationJobtestCommandService applicationJobtestCommandService;

    private final ApplicationJobtestQueryService applicationJobtestQueryService;


    // 실무테스트 등록
    public CreateJobtestResponseDTO createJobtest(CreateJobtestCommandDTO dto) {
        int jobtestId = jobtestCommandService.createJobtest(dto);

        List<CreateJobtestQuestionResponseDTO> questions = new ArrayList<>();
        if(dto.getJobtestQuestions() != null) {
            for(CreateJobtestQuestionCommandDTO question : dto.getJobtestQuestions()) {
                questions.add(jobtestQuestionCommandService.createJobtestQuestion(question, jobtestId));
            }
        }

        JobtestEntity jobtestEntity = jobtestCommandService.findById(jobtestId).orElseThrow(() -> new
                BusinessException(ResponseCode.EMPLOYMENT_INVALID_JOBTEST));
        return JobtestMapper.toCreateDto(jobtestEntity, questions);
    }

    // 실무테스트 채점
    public List<UpdateAnswerCommandDTO> gradeApplicationJobTest(int applicationJobTestId) {
        List<AnswerEntity> answers = answerCommandService.findByApplicationJobtestId(applicationJobTestId);
        List<UpdateAnswerCommandDTO> updateAnswers = new ArrayList<>();
        double totalScore = 0.0;

        for (AnswerEntity answer : answers) {
            QuestionEntity question = questionCommandService.findById(answer.getQuestionId())
                    .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_QUESTION));

            switch (question.getType()) {
                case MULTIPLE:      // 선택형
                case SUBJECTIVE:    // 단답형 x
                    answerCommandService.autoGrade(answer, question);
                    break;
                case DESCRIPTIVE:   // 서술형
                    answerCommandService.descriptiveGrade(answer, question);
                    break;
                default:
                    throw new BusinessException(ResponseCode.EMPLOYMENT_INVALID_QUESTION_TYPE);
            }
            updateAnswers.add(answerCommandService.updateAnswerEntity(answer));
            totalScore += answer.getScore();
        }

        // 지원서별 실무테스트 상태, 채점 총 점수 자동 변경
        applicationJobtestCommandService.updateGradingStatusAndScore(applicationJobTestId, totalScore);

        return updateAnswers;
    }

    public JobtestExamQueryDTO enterJobtestExam(int jobtestId, JobtestEntryRequestDTO requestDTO) {
        // 유효한 코드인지 확인
        int applicationJobtestId = applicationJobtestQueryService.verifyEntryCode(jobtestId, requestDTO);

        // 시험시간인지 확인
        jobtestQueryService.checkJobtestTime(jobtestId);

        // 입장했다면 시험 정보 반환
        JobtestExamQueryDTO examDTO = jobtestQueryService.enterJobtestExam(jobtestId, applicationJobtestId);

        return examDTO;
    }
}
