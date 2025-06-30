package com.piveguyz.empickbackend.employment.jobtests.answer.query.service;

import com.piveguyz.empickbackend.employment.jobtests.answer.query.dto.AnswerQueryDTO;

import java.util.List;

public interface AnswerQueryService {
    List<AnswerQueryDTO> findAnswerWithApplicationJobtestId(int applicationJobtestId);
}
