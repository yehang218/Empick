package com.piveguyz.empickbackend.employment.applicationItem.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.piveguyz.empickbackend.employment.applicationItem.query.dto.ApplicationItemQueryDTO;

@Mapper
public interface ApplicationItemQueryMapper {
	List<ApplicationItemQueryDTO> findByRecruitmentId(int recruitmentId);
}
