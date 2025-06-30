package com.piveguyz.empickbackend.employment.jobtests.question.command.domain.repository;

import com.piveguyz.empickbackend.employment.jobtests.question.command.domain.aggregate.QuestionOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOptionEntity, Integer> {
    int countByQuestionId(int questionId);

    @Modifying
    @Transactional
    @Query("DELETE FROM QuestionOptionEntity qo WHERE qo.questionId = :questionId")
    void deleteByQuestionId(@Param("questionId") Integer questionId);
}
