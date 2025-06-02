package com.piveguyz.empickbackend.interview.command.domain.repository;

import com.piveguyz.empickbackend.interview.command.domain.aggregate.InterviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewRepository extends JpaRepository<InterviewEntity, Integer> {
}
