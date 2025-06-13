package com.piveguyz.empickbackend.employment.interviews.facade;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.interviews.interview.command.application.dto.InterviewCommandDTO;
import com.piveguyz.empickbackend.employment.interviews.interview.command.application.service.InterviewCommandService;
import com.piveguyz.empickbackend.employment.interviews.interview.query.service.InterviewQueryService;
import com.piveguyz.empickbackend.employment.interviews.interviewCriteria.query.dto.InterviewCriteriaQueryDTO;
import com.piveguyz.empickbackend.employment.interviews.interviewCriteria.query.service.InterviewCriteriaQueryService;
import com.piveguyz.empickbackend.employment.interviews.interviewScore.command.application.dto.InterviewScoreCommandDTO;
import com.piveguyz.empickbackend.employment.interviews.interviewScore.command.application.service.InterviewScoreCommandService;
import com.piveguyz.empickbackend.employment.interviews.interviewScore.command.domain.aggregate.InterviewScoreEntity;
import com.piveguyz.empickbackend.employment.interviews.interviewScore.command.domain.repository.InterviewScoreRepository;
import com.piveguyz.empickbackend.employment.interviews.interviewScore.query.dto.InterviewScoreQueryDTO;
import com.piveguyz.empickbackend.employment.interviews.interviewScore.query.service.InterviewScoreQueryService;
import com.piveguyz.empickbackend.employment.interviews.interviewer.command.application.dto.InterviewerCommandDTO;
import com.piveguyz.empickbackend.employment.interviews.interviewer.command.application.mapper.InterviewerCommandMapper;
import com.piveguyz.empickbackend.employment.interviews.interviewer.command.application.service.InterviewerCommandService;
import com.piveguyz.empickbackend.employment.interviews.interviewer.command.domain.aggregate.InterviewerEntity;
import com.piveguyz.empickbackend.employment.interviews.interviewer.command.domain.repository.InterviewerRepository;
import com.piveguyz.empickbackend.employment.interviews.interviewer.query.dto.InterviewerQueryDTO;
import com.piveguyz.empickbackend.employment.interviews.interviewer.query.mapper.InterviewerMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class InterviewFacade {
    private final InterviewCommandService interviewCommandService;
    private final InterviewScoreQueryService interviewScoreQueryService;
    private final InterviewQueryService interviewQueryService;
    private final InterviewerRepository interviewerRepository;
    private final InterviewerCommandMapper interviewerCommandMapper;
    private final InterviewerCommandService interviewerCommandService;
    private final InterviewerMapper interviewerMapper;
    private final InterviewCriteriaQueryService interviewCriteriaQueryService;

    public InterviewCommandDTO createInterview(InterviewCommandDTO interviewCommandDTO){
        LocalDateTime interviewTime = interviewCommandDTO.getDatetime();
        boolean possible = interviewQueryService.checkAvailable(interviewTime);
        if(possible){
            InterviewCommandDTO createdDTO = interviewCommandService.create(interviewCommandDTO);
            return createdDTO;
        } else {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_IMPOSSIBLE_TIME);
        }
    }

    public InterviewCommandDTO deleteInterview(Integer id){
        List<InterviewerQueryDTO> interviewerQueryDTOList = interviewerMapper.findByInterviewId(id);
        if(!interviewerQueryDTOList.isEmpty()){
            throw new BusinessException(ResponseCode.BAD_REQUEST);
        }
        InterviewCommandDTO dto = interviewCommandService.delete(id);
        return dto;
    }

    public InterviewerCommandDTO updateInterviewerScore(Integer interviewerId) {
        InterviewerEntity interviewerEntity = interviewerRepository.findById(interviewerId)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEWER_NOT_FOUND));
        Double totalScore = (double) 0;
        List<InterviewScoreQueryDTO> interviewScoreDTOList = interviewScoreQueryService.findByInterviewerId(interviewerId);
        for(InterviewScoreQueryDTO scoreDTO : interviewScoreDTOList){
            Integer score = scoreDTO.getScore();
            Integer criteriaId = scoreDTO.getCriteriaId();
            InterviewCriteriaQueryDTO interviewCriteriaQueryDTO = interviewCriteriaQueryService.findById(criteriaId);
            Double weight = interviewCriteriaQueryDTO.getWeight();
            totalScore += score * weight;
        }
        interviewerEntity.setScore(totalScore);
        interviewerRepository.save(interviewerEntity);
        return interviewerCommandMapper.toDTO(interviewerEntity);
    }

    public InterviewerCommandDTO deleteInterviewer(Integer id) {
        List<InterviewScoreQueryDTO> dtoList = interviewScoreQueryService.findByInterviewerId(id);
        if(!dtoList.isEmpty()){
            throw new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEWER_ALREADY_SCORE_INPUT);
        }
//        for(InterviewScoreQueryDTO scoreDTO : dtoList){
//            Integer scoreId = scoreDTO.getId();
//            interviewScoreCommandService.delete(scoreId);
//        }
        InterviewerCommandDTO deletedDTO = interviewerCommandService.deleteInterviewer(id);
        return deletedDTO;
    }
}
