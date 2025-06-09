package com.piveguyz.empickbackend.job.command.domain.repository;

import com.piveguyz.empickbackend.job.command.domain.aggregate.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<JobEntity, Integer> {
}
