package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.repository;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate.JobtestQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobtestQuestionRepository extends JpaRepository<JobtestQuestionEntity, Integer> {

    boolean existsByJobTestIdAndQuestionId(int jobtestId, int questionId);

    @Query("SELECT MAX(q.optionNumber) FROM JobtestQuestionEntity q WHERE q.jobTestId = :jobTestId")
    Integer findMaxOptionNumberByJobTestId(@Param("jobTestId") int jobTestId);
}
