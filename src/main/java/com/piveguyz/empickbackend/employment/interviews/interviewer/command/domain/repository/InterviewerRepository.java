package com.piveguyz.empickbackend.employment.interviews.interviewer.command.domain.repository;


import com.piveguyz.empickbackend.employment.interviews.interviewer.command.domain.aggregate.InterviewerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewerRepository extends JpaRepository<InterviewerEntity, Integer> {

    boolean existsByInterviewIdAndInterviewerId(Integer interviewId, Integer interviewerId);
}
