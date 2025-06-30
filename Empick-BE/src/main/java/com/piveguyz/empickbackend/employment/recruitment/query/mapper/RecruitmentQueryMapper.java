package com.piveguyz.empickbackend.employment.recruitment.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.piveguyz.empickbackend.employment.applicant.query.dto.ApplicantQueryDTO;
import com.piveguyz.empickbackend.employment.applicant.query.dto.ApplicationQueryDTO;
import com.piveguyz.empickbackend.employment.recruitment.query.dto.RecruitmentQueryConditionDTO;
import com.piveguyz.empickbackend.employment.recruitment.query.dto.RecruitmentQueryDTO;

@Mapper
public interface RecruitmentQueryMapper {
	List<RecruitmentQueryDTO> findRecruitments(@Param("condition") RecruitmentQueryConditionDTO condition);

	RecruitmentQueryDTO findById(@Param("id") int id);

	List<ApplicationQueryDTO> selectApplicationsByRecruitmentId(int recruitmentId);
}
