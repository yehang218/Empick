package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.mapper;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.JobtestExamQueryDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.JobtestQueryDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.JobtestQuestionListQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface JobtestMapper {
    List<JobtestQuestionListQueryDTO> findAllJobTests();
    JobtestQueryDTO findJobTestWithApplications(@Param("id") int id);

    Map<String, Object> selectStartedAndEndedAtById(@Param("jobtestId") int jobtestId);

    JobtestExamQueryDTO selectJobtestExamQuery(
            @Param("jobtestId") int jobtestId,
            @Param("applicationJobTestId") int applicationJobTestId
    );
}
