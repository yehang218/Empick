package com.piveguyz.empickbackend.employment.applicationItem.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.piveguyz.empickbackend.employment.applicationItem.command.domain.aggregate.ApplicationItemCategory;

public interface ApplicationItemCategoryRepository extends JpaRepository<ApplicationItemCategory, Integer> {
}
