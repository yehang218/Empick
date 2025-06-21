package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.mapper;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.ApplicationJobtestQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Mapper
public interface ApplicationJobtestMapper {
    List<ApplicationJobtestQueryDTO> selectAllApplicationJobtest();
    Integer selectApplicationJobTestIdByJobTestIdAndEntryCode(
            @Param("jobtestId") int jobtestId,
            @Param("entryCode") String entryCode
    );

    Date selectSubmittedAtById(@Param("applicationJobtestId") int applicationJobtestId);
}
