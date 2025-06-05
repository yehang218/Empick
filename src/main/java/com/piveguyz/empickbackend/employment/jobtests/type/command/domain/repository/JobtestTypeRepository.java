package com.piveguyz.empickbackend.employment.jobtests.type.command.domain.repository;

import com.piveguyz.empickbackend.employment.jobtests.type.command.domain.aggregate.JobtestTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobtestTypeRepository extends JpaRepository<JobtestTypeEntity, Integer> {
}
