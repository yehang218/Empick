package com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.repository;

import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.aggregate.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerEntity, Integer> {
    Optional<AnswerEntity> findByApplicationJobTestIdAndQuestionId(int applicationJobTestId, int questionId);

    List<AnswerEntity> findByApplicationJobTestId(int applicationJobTestId);
}
