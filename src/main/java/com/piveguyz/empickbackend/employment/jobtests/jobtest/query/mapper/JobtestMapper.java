package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.mapper;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.ApplicationWithJobtestQueryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JobtestMapper {
    List<ApplicationWithJobtestQueryDTO> findApplicationsWithJobtestInfo();
}
