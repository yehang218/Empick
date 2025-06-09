package com.piveguyz.empickbackend.employment.recruitmentProcess.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.piveguyz.empickbackend.employment.recruitmentProcess.query.dto.RecruitmentProcessQueryDTO;

@Mapper
public interface RecruitmentProcessQueryMapper {

	List<RecruitmentProcessQueryDTO> findByRecruitmentId(int recruitmentId);
}