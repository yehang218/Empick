package com.piveguyz.empickbackend.employment.jobtests.question.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.repository.AnswerRepository;
import com.piveguyz.empickbackend.employment.jobtests.grading.command.application.mapper.GradingCriteriaMapper;
import com.piveguyz.empickbackend.employment.jobtests.grading.command.domain.repository.GradingCriteriaRepository;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.repository.JobtestQuestionRepository;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.repository.JobtestRepository;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto.CreateQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto.CreateQuestionOptionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto.DeleteQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto.UpdateQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.mapper.QuestionMapper;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.mapper.QuestionOptionMapper;
import com.piveguyz.empickbackend.employment.jobtests.question.command.domain.aggregate.QuestionEntity;
import com.piveguyz.empickbackend.employment.jobtests.question.command.domain.aggregate.QuestionOptionEntity;
import com.piveguyz.empickbackend.employment.jobtests.question.command.domain.aggregate.enums.QuestionType;
import com.piveguyz.empickbackend.employment.jobtests.question.command.domain.repository.QuestionOptionRepository;
import com.piveguyz.empickbackend.employment.jobtests.question.command.domain.repository.QuestionRepository;
import com.piveguyz.empickbackend.orgstructure.member.command.domain.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionCommandServiceImpl implements QuestionCommandService {

    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final GradingCriteriaRepository gradingCriteriaRepository;
    private final MemberRepository memberRepository;
    private final AnswerRepository answerRepository;
    private final JobtestRepository jobtestRepository;
    private final JobtestQuestionRepository jobtestQuestionRepository;


    // 실무 테스트 문제 등록
    @Override
    public int createQuestion(CreateQuestionCommandDTO createQuestionCommandDTO) {
        // 이미 존재할 경우
        if (questionRepository.existsByContent(createQuestionCommandDTO.getContent())) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_QUESTION_DUPLICATE);
        }

        // 작성자가 없는 회원인 경우
        if (!memberRepository.existsById(createQuestionCommandDTO.getCreatedMemberId())) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INVALID_MEMBER);
        }

        QuestionEntity questionEntity = QuestionMapper.toEntity(createQuestionCommandDTO);
        QuestionEntity saved = questionRepository.save(questionEntity);

        return saved.getId();
    }


    // 실무 테스트 문제 수정
    @Override
    public UpdateQuestionCommandDTO updateQuestion(int id, UpdateQuestionCommandDTO updateQuestionCommandDTO) {
        // 문제 있는지 확인
        QuestionEntity question = questionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_QUESTION));

        // 수정자가 없는 회원이라면
        if (!memberRepository.existsById(updateQuestionCommandDTO.getUpdatedMemberId())) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INVALID_UPDATED_MEMBER);
        }

        question.updateQuestionEntity(updateQuestionCommandDTO);

        QuestionEntity updatedEntity = questionRepository.save(question);

        // 4. 선택형 문제라면 기존 선택지 삭제 후 재등록
        if (question.getType() == QuestionType.MULTIPLE) {
            // 기존 선택지 삭제
            questionOptionRepository.deleteByQuestionId(id);

            // 새 선택지 저장
            List<QuestionOptionEntity> newOptions = new ArrayList<>();
            List<CreateQuestionOptionCommandDTO> optionDTOs = updateQuestionCommandDTO.getQuestionOptions();

            for (int i = 0; i < optionDTOs.size(); i++) {
                CreateQuestionOptionCommandDTO dto = optionDTOs.get(i);
                QuestionOptionEntity entity = QuestionOptionMapper.toEntity(dto, i + 1, id);
                newOptions.add(entity);
            }

            questionOptionRepository.saveAll(newOptions);
        }

        // 5. 🚩TODO : 서술형 문제라면 기존 채점 기준 삭제 후 재등록


        return QuestionMapper.toUpdateDto(updatedEntity);
    }

    // 실무 테스트 문제 삭제
    @Override
    public DeleteQuestionCommandDTO deleteQuestion(int id) {
        // 문제 있는지 확인
        QuestionEntity question = questionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_QUESTION));

        // 답안에서 참조중이라면
        if(answerRepository.existsByQuestionId(id)) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_QUESTION_USED_IN_ANSWER);
        }

        // 실무테스트에서 참조중이라면
        if(jobtestQuestionRepository.existsByQuestionId(id)) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_QUESTION_USED_IN_JOBTEST);
        }

        try {
            questionRepository.delete(question);
        } catch (DataIntegrityViolationException e) {
            // 참조중이라면 EMPLOYMENT_QUESTION_DELETE_CONFLICT 에러 발생
            throw new BusinessException(ResponseCode.EMPLOYMENT_QUESTION_DELETE_CONFLICT);
        }

        return QuestionMapper.toDeleteDto(question);
    }

    @Override
    public Optional<QuestionEntity> findById(int questionId) {
        return questionRepository.findById(questionId);
    }
}
