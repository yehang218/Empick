package com.piveguyz.empickbackend.employment.interviews.facade;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.interviews.interview.command.application.dto.InterviewCommandDTO;
import com.piveguyz.empickbackend.employment.interviews.interview.command.application.service.InterviewCommandService;
import com.piveguyz.empickbackend.employment.interviews.interview.query.service.InterviewQueryService;
import com.piveguyz.empickbackend.employment.interviews.interviewCriteria.command.application.service.InterviewCriteriaCommandService;
import com.piveguyz.empickbackend.employment.interviews.interviewCriteria.query.dto.InterviewCriteriaQueryDTO;
import com.piveguyz.empickbackend.employment.interviews.interviewCriteria.query.service.InterviewCriteriaQueryService;
import com.piveguyz.empickbackend.employment.interviews.interviewScore.command.application.dto.InterviewScoreCommandDTO;
import com.piveguyz.empickbackend.employment.interviews.interviewScore.command.application.service.InterviewScoreCommandService;
import com.piveguyz.empickbackend.employment.interviews.interviewScore.command.domain.aggregate.InterviewScoreEntity;
import com.piveguyz.empickbackend.employment.interviews.interviewScore.command.domain.repository.InterviewScoreRepository;
import com.piveguyz.empickbackend.employment.interviews.interviewScore.query.dto.InterviewScoreQueryDTO;
import com.piveguyz.empickbackend.employment.interviews.interviewScore.query.mapper.InterviewScoreMapper;
import com.piveguyz.empickbackend.employment.interviews.interviewScore.query.service.InterviewScoreQueryService;
import com.piveguyz.empickbackend.employment.interviews.interviewer.command.application.dto.InterviewerCommandDTO;
import com.piveguyz.empickbackend.employment.interviews.interviewer.command.application.mapper.InterviewerCommandMapper;
import com.piveguyz.empickbackend.employment.interviews.interviewer.command.application.service.InterviewerCommandService;
import com.piveguyz.empickbackend.employment.interviews.interviewer.command.domain.aggregate.InterviewerEntity;
import com.piveguyz.empickbackend.employment.interviews.interviewer.command.domain.repository.InterviewerRepository;
import com.piveguyz.empickbackend.employment.interviews.interviewer.query.dto.InterviewerQueryDTO;
import com.piveguyz.empickbackend.employment.interviews.interviewer.query.mapper.InterviewerMapper;
import com.piveguyz.empickbackend.employment.interviews.interviewer.query.service.InterviewerQueryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class InterviewFacade {
    private final InterviewCommandService interviewCommandService;
    private final InterviewQueryService interviewQueryService;
    private final InterviewScoreCommandService interviewScoreCommandService;
    private final InterviewScoreQueryService interviewScoreQueryService;
    private final InterviewScoreRepository interviewScoreRepository;
    private final InterviewScoreMapper interviewScoreMapper;
    private final InterviewerCommandService interviewerCommandService;
    private final InterviewerQueryService interviewerQueryService;
    private final InterviewerRepository interviewerRepository;
    private final InterviewerCommandMapper interviewerCommandMapper;
    private final InterviewerMapper interviewerMapper;
    private final InterviewCriteriaCommandService interviewCriteriaCommandService;
    private final InterviewCriteriaQueryService interviewCriteriaQueryService;

    public InterviewCommandDTO createInterview(InterviewCommandDTO interviewCommandDTO){
        LocalDateTime interviewTime = interviewCommandDTO.getDatetime();
        boolean timeAvailable = interviewQueryService.checkAvailable(interviewTime);
        if(timeAvailable){
            InterviewCommandDTO createdDTO = interviewCommandService.create(interviewCommandDTO);
            return createdDTO;
        } else {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_IMPOSSIBLE_TIME);
        }
    }

    public InterviewCommandDTO updateInterview(Integer id, InterviewCommandDTO dto) {
        LocalDateTime interviewTime = dto.getDatetime();
        Boolean timeAvailable = interviewQueryService.checkAvailable(interviewTime);
        if(timeAvailable){
            InterviewCommandDTO updatedDTO = interviewCommandService.update(id, dto);
            return updatedDTO;
        } else {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_IMPOSSIBLE_TIME);
        }
    }

    public InterviewCommandDTO updateDateTime(Integer id, LocalDateTime datetime) {
        Boolean timeAvailable = interviewQueryService.checkAvailable(datetime);
        if(timeAvailable){
            InterviewCommandDTO updatedDTO = interviewCommandService.updateDateTime(id, datetime);
            return updatedDTO;
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

    public Double calculateInterviewerScore(Integer interviewerId){
        List<InterviewScoreQueryDTO> dtoList = interviewScoreQueryService.findByInterviewerId(interviewerId);
        Integer count = dtoList.size();
        Double sum = (double)0;
        for(InterviewScoreQueryDTO dto : dtoList){
            Integer score = dto.getScore();
            Integer criteriaId = dto.getCriteriaId();
            InterviewCriteriaQueryDTO criteriaDTO = interviewCriteriaQueryService.findById(criteriaId);
            Double weight = criteriaDTO.getWeight();
            sum += weight * score;
        }
        return sum / count;
    }

    public Double calculateInterviewScore(Integer interviewId){
        List<InterviewerQueryDTO> dtoList = interviewerQueryService.findByInterviewId(interviewId);
        Integer count = dtoList.size();
        Double sum = (double)0;
        for(InterviewerQueryDTO dto : dtoList){
            Double score = dto.getScore();
            sum += score;
        }
        return sum / count;
    }

    public InterviewScoreCommandDTO createInterviewScore(InterviewScoreCommandDTO dto) {
        InterviewScoreCommandDTO createdDTO = interviewScoreCommandService.create(dto);
        Integer interviewerId = dto.getInterviewerId();
        Double newInterviewerScore = calculateInterviewerScore(interviewerId);

        interviewerCommandService.updateInterviewerScore(interviewerId, newInterviewerScore);

        InterviewerQueryDTO interviewerQueryDTO = interviewerQueryService.findById(interviewerId);
        Integer interviewId = interviewerQueryDTO.getInterviewId();
        Double newInterviewScore = calculateInterviewScore(interviewId);

        interviewCommandService.updateScore(interviewId, newInterviewScore);

        return createdDTO;
    }

    public InterviewScoreCommandDTO updateInterviewScore(Integer id, InterviewScoreCommandDTO dto) {
        InterviewScoreCommandDTO updatedDTO = interviewScoreCommandService.update(id, dto);
        Integer interviewerId = dto.getInterviewerId();
        Double newInterviewerScore = calculateInterviewerScore(interviewerId);

        interviewerCommandService.updateInterviewerScore(interviewerId, newInterviewerScore);

        InterviewerQueryDTO interviewerQueryDTO = interviewerQueryService.findById(interviewerId);
        Integer interviewId = interviewerQueryDTO.getInterviewId();
        Double newInterviewScore = calculateInterviewScore(interviewId);

        interviewCommandService.updateScore(interviewId, newInterviewScore);

        return updatedDTO;
    }
}
