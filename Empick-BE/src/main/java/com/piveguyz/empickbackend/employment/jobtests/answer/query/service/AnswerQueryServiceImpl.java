package com.piveguyz.empickbackend.employment.jobtests.answer.query.service;

import com.piveguyz.empickbackend.employment.jobtests.answer.query.dto.AnswerQueryDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.query.mapper.AnswerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerQueryServiceImpl implements AnswerQueryService {
    private final AnswerMapper answerMapper;

    @Override
    public List<AnswerQueryDTO> findAnswerWithApplicationJobtestId(int applicationJobtestId) {
        return answerMapper.findAnswerWithApplicationJobtestId(applicationJobtestId);
    }
}
