package com.piveguyz.empickbackend.employment.applicant.command.domain.repository;


import com.piveguyz.empickbackend.employment.applicant.command.domain.aggregate.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Integer> {


    boolean existsByRecruitmentIdAndApplicantId(int recruitmentId, int applicantId);
}
