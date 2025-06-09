package com.piveguyz.empickbackend.employment.jobtests.answer.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.CreateAnswerCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.CreateAnswerResponseDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.UpdateAnswerCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.aggregate.AnswerEntity;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.repository.AnswerRepository;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.mapper.AnswerMapper;

import org.springframework.stereotype.Service;

@Service
public class AnswerCommandServiceImpl implements AnswerCommandService {

    private final AnswerRepository answerRepository;

    public AnswerCommandServiceImpl(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    // 선택지 등록
    @Override
    public CreateAnswerResponseDTO createAnswer(CreateAnswerCommandDTO createAnswerCommandDTO) {
        int jobTestId = createAnswerCommandDTO.getApplicationJobTestId();
        int questionId = createAnswerCommandDTO.getQuestionId();

        // 이미 같은 문제에 대한 답이 존재하는 경우 대비
        Integer maxAttempt = answerRepository.findMaxAttempt(jobTestId, questionId);
        int nextAttempt = (maxAttempt == null) ? 1 : maxAttempt + 1;

        AnswerEntity entity = AnswerMapper.toEntity(createAnswerCommandDTO, nextAttempt);
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

    // 선택지 삭제
    @Override
    public Integer deleteAnswer(int id) {
        AnswerEntity answer = answerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_JOBTEST_ANSWER));
        answerRepository.delete(answer);
        return answer.getId();
    }
}
