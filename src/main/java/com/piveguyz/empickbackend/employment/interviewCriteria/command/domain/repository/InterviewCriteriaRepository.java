package com.piveguyz.empickbackend.employment.interviewCriteria.command.domain.repository;


import com.piveguyz.empickbackend.employment.interviewCriteria.command.domain.aggregate.InterviewCriteriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface InterviewCriteriaRepository extends JpaRepository<InterviewCriteriaEntity, Integer> {
}
