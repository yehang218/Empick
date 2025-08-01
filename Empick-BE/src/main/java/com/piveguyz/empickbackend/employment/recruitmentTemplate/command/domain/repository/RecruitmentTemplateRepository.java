package com.piveguyz.empickbackend.employment.recruitmentTemplate.command.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.piveguyz.empickbackend.employment.recruitmentTemplate.command.domain.aggregate.RecruitmentTemplate;

public interface RecruitmentTemplateRepository extends JpaRepository<RecruitmentTemplate, Integer> {
	Optional<RecruitmentTemplate> findByIdAndIsDeletedFalse(int recruitmentTemplateId);
}
