package com.piveguyz.empickbackend.employment.jobtests.evaluation.command.domain.repository;

import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.domain.aggregate.EvaluationCriteriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationCriteriaRepository extends JpaRepository<EvaluationCriteriaEntity, Integer> {
}
