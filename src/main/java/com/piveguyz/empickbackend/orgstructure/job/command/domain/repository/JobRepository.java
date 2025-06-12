package com.piveguyz.empickbackend.orgstructure.job.command.domain.repository;

import com.piveguyz.empickbackend.orgstructure.job.command.domain.aggregate.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<JobEntity, Integer> {
}
