package com.piveguyz.empickbackend.employment.jobtests.question.command.application.service;

import com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto.CreateQuestionOptionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto.CreateQuestionOptionResponse;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto.UpdateQuestionOptionCommandDTO;
import jakarta.validation.Valid;

public interface QuestionOptionCommandService {
    CreateQuestionOptionResponse createQuestionOption(@Valid CreateQuestionOptionCommandDTO createQuestionOptionCommandDTO, int questionId);

    UpdateQuestionOptionCommandDTO updateQuestionOption(int id, @Valid UpdateQuestionOptionCommandDTO updateQuestionOptionCommandDTO);

    Integer deleteQuestionOption(int id);
}
