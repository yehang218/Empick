package com.piveguyz.empickbackend.employment.recruitmentTemplateCopy.command.domain.repository;

import org.springframework.data.repository.CrudRepository;

import com.piveguyz.empickbackend.employment.recruitmentTemplateCopy.command.domain.aggregate.RecruitmentTemplateCopy;

public interface RecruitmentTemplateCopyRepository extends CrudRepository<RecruitmentTemplateCopy, Integer> {
	void deleteByRecruitmentId(int recruitmentId);
}
