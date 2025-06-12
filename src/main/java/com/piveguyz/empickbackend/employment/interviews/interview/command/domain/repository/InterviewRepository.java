package com.piveguyz.empickbackend.employment.interviews.interview.command.domain.repository;

import com.piveguyz.empickbackend.employment.interviews.interview.command.domain.aggregate.InterviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<InterviewEntity, Integer> {
}
