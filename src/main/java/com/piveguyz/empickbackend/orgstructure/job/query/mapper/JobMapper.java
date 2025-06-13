package com.piveguyz.empickbackend.orgstructure.job.query.mapper;

import com.piveguyz.empickbackend.orgstructure.job.query.dto.JobResponseDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JobMapper {
    List<JobResponseDTO> selectAllJobs();
}
