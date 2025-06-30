package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.JobtestExamQueryDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.JobtestQueryDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.JobtestQuestionListQueryDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.mapper.JobtestMapper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import java.util.Map;

@Service
public class JobtestQueryServiceImpl implements JobtestQueryService {
    private final JobtestMapper jobtestMapper;

    JobtestQueryServiceImpl(JobtestMapper jobtestMapper) {
        this.jobtestMapper = jobtestMapper;
    }

    @Override
    public List<JobtestQuestionListQueryDTO> getAllJobTests() {
        return jobtestMapper.findAllJobTests();
    }

    @Override
    public JobtestQueryDTO findJobTestWithApplications(int id) {
        return jobtestMapper.findJobTestWithApplications(id);
    }

    // 현재 시간이 시험 시간에 포함되는 시간인지 확인
    @Override
    public void checkJobtestTime(int jobtestId) {
        Map<String, Object> map = jobtestMapper.selectStartedAndEndedAtById(jobtestId);

        if (map == null || map.get("started_at") == null || map.get("ended_at") == null) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INVALID_JOBTEST);
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startedAt = ((Timestamp) map.get("started_at")).toLocalDateTime();
        LocalDateTime endedAt   = ((Timestamp) map.get("ended_at")).toLocalDateTime();

        if (now.isBefore(startedAt) || now.isAfter(endedAt)) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INVALID_TIME);
        }
    }

    @Override
    public JobtestExamQueryDTO enterJobtestExam(int jobtestId, int applicationJobTestId) {
        JobtestExamQueryDTO examDTO = jobtestMapper.selectJobtestExamQuery(jobtestId, applicationJobTestId);
        return examDTO;
    }

}
