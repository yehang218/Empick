package com.piveguyz.empickbackend.employment.recruitmentProcess.command.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.piveguyz.empickbackend.employment.recruitmentProcess.command.domain.aggregate.RecruitmentProcess;

public interface RecruitmentProcessRepository extends JpaRepository<RecruitmentProcess, Long> {

	List<RecruitmentProcess> findAllByRecruitmentId(int recruitmentId);
}