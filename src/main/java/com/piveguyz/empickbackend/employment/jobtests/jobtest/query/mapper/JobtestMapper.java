package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.mapper;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.JobtestApplicationsDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.JobtestQuestionListQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JobtestMapper {
    List<JobtestQuestionListQueryDTO> findAllJobTests();
    JobtestApplicationsDTO findJobTestWithApplications(@Param("id") int id);
}
