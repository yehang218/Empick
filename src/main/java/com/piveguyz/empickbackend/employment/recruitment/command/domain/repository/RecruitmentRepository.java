package com.piveguyz.empickbackend.employment.recruitment.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.piveguyz.empickbackend.employment.recruitment.command.domain.aggregate.Recruitment;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Integer> {
}
