package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.service;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.JobtestEntryRequestDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.JobtestExamQueryDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.JobtestQueryDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.JobtestQuestionListQueryDTO;

import java.util.List;

public interface JobtestQueryService {
    List<JobtestQuestionListQueryDTO> getAllJobTests();
    JobtestQueryDTO findJobTestWithApplications(int id);

    void checkJobtestTime(int jobtestId);

    JobtestExamQueryDTO enterJobtestExam(int jobtestId, int applicationJobTestId);
}
