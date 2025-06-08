package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.repository;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate.JobtestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobtestRepository extends JpaRepository<JobtestEntity, Integer> {
    boolean existsByTitle(String title);
}
