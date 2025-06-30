package com.piveguyz.empickbackend.employment.jobtests.evaluation.command.domain.repository;

import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.domain.aggregate.EvaluationResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationResultRepository extends JpaRepository<EvaluationResultEntity, Integer> {
}
