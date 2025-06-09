package com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.repository;

import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.aggregate.GradingResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradingResultRepository extends JpaRepository<GradingResultEntity, Integer> {
}
