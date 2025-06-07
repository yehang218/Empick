package com.piveguyz.empickbackend.employment.applicationItem.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.piveguyz.empickbackend.employment.applicationItem.command.domain.aggregate.ApplicationItem;

public interface ApplicationItemRepository extends JpaRepository<ApplicationItem, Integer> {
	boolean existsByRecruitmentIdAndCategoryId(int recruitmentId, int categoryId);
}
