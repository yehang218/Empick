package com.piveguyz.empickbackend.employment.recruitmentTemplateCopy.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.piveguyz.empickbackend.employment.recruitmentTemplateCopy.query.dto.RecruitmentTemplateCopyQueryDTO;

@Mapper
public interface RecruitmentTemplateCopyQueryMapper {
	List<RecruitmentTemplateCopyQueryDTO> findByRecruitmentId(int recruitmentId);
}
