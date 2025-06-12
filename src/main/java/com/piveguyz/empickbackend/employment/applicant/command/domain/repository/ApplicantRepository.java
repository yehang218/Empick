package com.piveguyz.empickbackend.employment.applicant.command.domain.repository;

import com.piveguyz.empickbackend.employment.applicant.command.domain.aggregate.ApplicantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<ApplicantEntity, Integer> {
        boolean existsByEmail(String email);
        boolean existsByPhone(String phone);
    }


