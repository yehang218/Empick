package com.piveguyz.empickbackend.employment.jobtests.answer.command.application.service;

import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.CreateAnswerCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.CreateAnswerResponseDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.UpdateAnswerCommandDTO;
import jakarta.validation.Valid;

public interface AnswerCommandService {
    CreateAnswerResponseDTO createAnswer(@Valid CreateAnswerCommandDTO createAnswerCommandDTO);

    UpdateAnswerCommandDTO updateAnswerCommandDTO(int id, UpdateAnswerCommandDTO updateAnswerCommandDTO);

    Integer deleteAnswer(int id);

}
