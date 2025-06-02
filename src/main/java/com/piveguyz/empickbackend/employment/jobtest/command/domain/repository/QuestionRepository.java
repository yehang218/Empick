package com.piveguyz.empickbackend.employment.jobtest.command.domain.repository;

import com.piveguyz.empickbackend.employment.jobtest.command.domain.aggregate.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Integer> {

}
