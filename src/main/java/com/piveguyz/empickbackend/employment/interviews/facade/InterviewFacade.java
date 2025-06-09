package com.piveguyz.empickbackend.employment.interviews.facade;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.interviews.interview.command.application.dto.InterviewCommandDTO;
import com.piveguyz.empickbackend.employment.interviews.interview.command.application.service.InterviewCommandService;
import com.piveguyz.empickbackend.employment.interviews.interview.query.service.InterviewQueryService;
import com.piveguyz.empickbackend.employment.interviews.interviewScore.command.application.service.InterviewScoreCommandService;
import com.piveguyz.empickbackend.employment.interviews.interviewScore.query.dto.InterviewScoreQueryDTO;
import com.piveguyz.empickbackend.employment.interviews.interviewScore.query.service.InterviewScoreQueryService;

import java.time.LocalDateTime;
import java.util.List;

public class InterviewFacade {
    private final InterviewScoreCommandService interviewScoreCommandService;
    private final InterviewScoreQueryService interviewScoreQueryService;
    private final InterviewCommandService interviewCommandService;
    private final InterviewQueryService interviewQueryService;

    public InterviewFacade(InterviewScoreQueryService interviewScoreQueryService,
                           InterviewScoreCommandService interviewScoreCommandService,
                           InterviewCommandService interviewCommandService, InterviewQueryService interviewQueryService) {
        this.interviewScoreCommandService = interviewScoreCommandService;
        this.interviewScoreQueryService = interviewScoreQueryService;
        this.interviewCommandService = interviewCommandService;
        this.interviewQueryService = interviewQueryService;
    }

    public InterviewCommandDTO createInterview(InterviewCommandDTO interviewCommandDTO){
        LocalDateTime interviewTime = interviewCommandDTO.getDate();
        boolean possible = interviewQueryService.checkAvailable(interviewTime);
        if(possible){
            InterviewCommandDTO createdDTO = interviewCommandService.create(interviewCommandDTO);
            return createdDTO;
        } else {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_IMPOSSIBLE_TIME);
        }
    }

    public InterviewCommandDTO deleteInterview(Integer id){
        List<InterviewScoreQueryDTO> interviewScoreQueryDTOList = interviewScoreQueryService.findByInterviewId(id);
        for(InterviewScoreQueryDTO interviewScoreQueryDTO : interviewScoreQueryDTOList){
            Integer interviewScoreId = interviewScoreQueryDTO.getId();
            interviewScoreCommandService.delete(interviewScoreId);
        }
        InterviewCommandDTO dto = interviewCommandService.delete(id);
        return dto;
    }
}
