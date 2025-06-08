package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.repository;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate.ApplicationJobtestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationJobtestRepository extends JpaRepository<ApplicationJobtestEntity, Integer> {
}
