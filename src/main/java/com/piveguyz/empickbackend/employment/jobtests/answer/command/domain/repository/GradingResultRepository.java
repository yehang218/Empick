package com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.repository;

import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.aggregate.GradingResultEntity;
import com.piveguyz.empickbackend.employment.jobtests.grading.command.domain.aggregate.GradingCriteriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradingResultRepository extends JpaRepository<GradingResultEntity, Integer> {
    List<GradingResultEntity> findByAnswerId(int id);
}
