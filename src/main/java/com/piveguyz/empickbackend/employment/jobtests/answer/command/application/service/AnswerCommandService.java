package com.piveguyz.empickbackend.employment.jobtests.answer.command.application.service;

import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.CreateAnswerCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.CreateAnswerResponseDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.UpdateAnswerCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.aggregate.AnswerEntity;
import com.piveguyz.empickbackend.employment.jobtests.question.command.domain.aggregate.QuestionEntity;
import jakarta.validation.Valid;

import java.util.List;

public interface AnswerCommandService {
    CreateAnswerResponseDTO createAnswer(@Valid CreateAnswerCommandDTO createAnswerCommandDTO);

    UpdateAnswerCommandDTO updateAnswerCommandDTO(int id, UpdateAnswerCommandDTO updateAnswerCommandDTO);
    UpdateAnswerCommandDTO updateAnswerEntity(AnswerEntity answer);

    Integer deleteAnswer(int id);

    void autoGrade(AnswerEntity answer, QuestionEntity question);
    void descriptiveGrade(AnswerEntity answer, QuestionEntity question);

    List<AnswerEntity> findByApplicationJobtestId(int applicationJobTestId);
}
