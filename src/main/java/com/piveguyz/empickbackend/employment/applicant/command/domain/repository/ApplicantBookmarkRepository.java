package com.piveguyz.empickbackend.employment.applicant.command.domain.repository;

import com.piveguyz.empickbackend.employment.applicant.command.domain.aggregate.ApplicantBookmarkEntity;
import com.piveguyz.empickbackend.employment.applicant.command.domain.aggregate.ApplicantBookmarkId;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ApplicantBookmarkRepository extends JpaRepository<ApplicantBookmarkEntity, ApplicantBookmarkId> {

}

