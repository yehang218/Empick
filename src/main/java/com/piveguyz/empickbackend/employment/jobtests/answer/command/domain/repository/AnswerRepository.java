package com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.repository;

import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.aggregate.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerEntity, Integer> {
    @Query("SELECT MAX(a.attempt) FROM AnswerEntity a WHERE a.applicationJobTestId = :jobTestId AND a.questionId = :questionId")
    Integer findMaxAttempt(@Param("jobTestId") int jobTestId, @Param("questionId") int questionId);

    List<AnswerEntity> findLatestAnswersByApplicationJobTestId(int applicationJobTestId);
}
