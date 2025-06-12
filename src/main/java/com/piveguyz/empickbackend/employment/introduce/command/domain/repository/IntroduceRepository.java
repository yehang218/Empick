package com.piveguyz.empickbackend.employment.introduce.command.domain.repository;

import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntroduceRepository extends JpaRepository<IntroduceEntity, Integer> {
    boolean existsByApplicantIdAndIntroduceTemplateId(Integer applicantId, Integer introduceTemplateId);
}
