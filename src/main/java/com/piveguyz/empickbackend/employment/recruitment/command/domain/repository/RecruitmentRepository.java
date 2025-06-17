package com.piveguyz.empickbackend.employment.recruitment.command.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.piveguyz.empickbackend.employment.recruitment.command.domain.aggregate.Recruitment;
import com.piveguyz.empickbackend.employment.recruitment.command.domain.enums.RecruitmentStatus;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Integer> {
	List<Recruitment> findAllByStatusAndStartedAtBefore(RecruitmentStatus recruitmentStatus, LocalDateTime now);

	List<Recruitment> findAllByStatusAndEndedAtBefore(RecruitmentStatus recruitmentStatus, LocalDateTime now);
}
