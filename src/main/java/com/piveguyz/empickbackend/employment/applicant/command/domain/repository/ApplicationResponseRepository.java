package com.piveguyz.empickbackend.employment.applicant.command.domain.repository;

import com.piveguyz.empickbackend.employment.applicant.command.domain.aggregate.ApplicationResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationResponseRepository extends JpaRepository<ApplicationResponseEntity, Integer> {

    boolean existsByApplicationIdAndApplicationItemId(int applicationId, int applicationItemId);
}
