package com.piveguyz.empickbackend.employment.interviewScore.command.domain.repository;

import com.piveguyz.empickbackend.employment.interviewScore.command.domain.aggregate.InterviewScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface InterviewScoreRepository extends JpaRepository<InterviewScoreEntity, Integer> {

    boolean existsByInterviewIdAndInterviewerIdAndItemId(Integer interviewId, Integer interviewerId, Integer itemId);
}
