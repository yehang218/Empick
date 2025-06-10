package com.piveguyz.empickbackend.employment.interviews.interviewScore.command.domain.repository;

import com.piveguyz.empickbackend.employment.interviews.interviewScore.command.domain.aggregate.InterviewScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewScoreRepository extends JpaRepository<InterviewScoreEntity, Integer> {

    boolean existsByInterviewerIdAndItemId(Integer interviewerId, Integer itemId);
}
