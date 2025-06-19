package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.JobtestEntryRequestDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate.ApplicationJobtestEntity;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate.JobtestEntity;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.JobtestExamQueryDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.mapper.ApplicationJobtestMapper;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.ApplicationJobtestQueryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationJobtestQueryServiceImpl implements ApplicationJobtestQueryService {
    private final ApplicationJobtestMapper applicationJobtestMapper;

    @Override
    public List<ApplicationJobtestQueryDTO> getAllApplicationJobTests() {
        return applicationJobtestMapper.selectAllApplicationJobtest();
    }

    @Override
    public int verifyEntryCode(int jobtestId, JobtestEntryRequestDTO requestDTO) {
        Integer applicationJobTestId = applicationJobtestMapper.selectApplicationJobTestIdByJobTestIdAndEntryCode(
                jobtestId, requestDTO.getEntryCode()
        );
        if (applicationJobTestId == null) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_JOBTEST_INVALID_ENTRY_CODE);
        }
        return applicationJobTestId;
    }

}
