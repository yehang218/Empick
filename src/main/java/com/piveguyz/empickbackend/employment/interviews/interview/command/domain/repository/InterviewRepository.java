package com.piveguyz.empickbackend.employment.interview.command.domain.repository;

import com.piveguyz.empickbackend.employment.interview.command.domain.aggregate.InterviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface InterviewRepository extends JpaRepository<InterviewEntity, Integer> {
}
