package com.piveguyz.empickbackend.employment.jobtests.grading.command.domain.repository;

import com.piveguyz.empickbackend.employment.jobtests.grading.command.domain.aggregate.GradingCriteriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradingCriteriaRepository extends JpaRepository<GradingCriteriaEntity, Integer> {
}
