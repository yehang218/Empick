package com.piveguyz.empickbackend.facade;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.UpdateAnswerCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.service.AnswerCommandService;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.aggregate.AnswerEntity;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.service.QuestionCommandService;
import com.piveguyz.empickbackend.employment.jobtests.question.command.domain.aggregate.QuestionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JobtestFacade {
    private final AnswerCommandService answerCommandService;
    private final QuestionCommandService questionCommandService;

    // 실무테스트 채점
    public List<UpdateAnswerCommandDTO> gradeApplicationJobTest(int applicationJobTestId) {
        List<AnswerEntity> answers = answerCommandService.findLatestAnswersByApplicationJobTestId(applicationJobTestId);
        List<UpdateAnswerCommandDTO> updateAnswers = new ArrayList<>();

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
        }
        return updateAnswers;
    }
}
