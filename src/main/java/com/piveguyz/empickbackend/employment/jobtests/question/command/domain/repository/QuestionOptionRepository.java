package com.piveguyz.empickbackend.employment.jobtests.question.command.domain.repository;

import com.piveguyz.empickbackend.employment.jobtests.question.command.domain.aggregate.QuestionOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOptionEntity, Integer> {
    int countByQuestionId(int questionId);

    // 문제별 가장 큰 선택지 번호
    Optional<QuestionOptionEntity> findTopByQuestionIdOrderByOptionNumberDesc(int questionId);

}
