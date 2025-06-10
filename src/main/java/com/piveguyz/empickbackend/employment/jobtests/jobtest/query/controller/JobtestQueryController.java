package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.JobtestApplicationsDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.JobtestQuestionListQueryDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.service.JobtestQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "실무테스트 API", description = "실무테스트 관련 API")
@RestController
@RequestMapping("/api/v1/employment/jobtests")
public class JobtestQueryController {
    private final JobtestQueryService jobtestQueryService;

    JobtestQueryController(JobtestQueryService jobtestQueryService) {
        this.jobtestQueryService = jobtestQueryService;
    }

    // 전체 실무테스트 조회
    @Operation(
            summary = "전체 실무테스트 조회",
            description = """
                     전체 실무테스트를 조회합니다.
                    """
    )
    @GetMapping
    public ResponseEntity<CustomApiResponse<List<JobtestQuestionListQueryDTO>>> getAllApplicationJobTests() {
        List<JobtestQuestionListQueryDTO> result = jobtestQueryService.getAllJobTests();
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, result));
    }

    // 실무테스트 상세 조회
    @Operation(
            summary = "실무테스트 상세 조회",
            description = """
                     id에 해당하는 실무테스트를 조회합니다.
                    """
    )
    @GetMapping("/{id}")
    public ResponseEntity<JobtestApplicationsDTO> getJobTestById(@PathVariable int id) {
        JobtestApplicationsDTO result = jobtestQueryService.findJobTestWithApplications(id);
        return ResponseEntity.ok(result);
    }
}
