package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto.ApplicationJobtestQueryDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.query.service.ApplicationJobtestQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "실무테스트 API", description = "실무테스트 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employment/application-jobtests")
public class ApplicationJobtestQueryController {

    private final ApplicationJobtestQueryService applicationJobtestQueryService;

    // 지원서별 실무테스트 전체 목록 조회
    @Operation(
            summary = "지원서별 실무테스트 전체 목록 조회",
            description = """
                    지원서별 실무테스트 전체 목록을 조회합니다.
                    """
    )
    @GetMapping
    public ResponseEntity<CustomApiResponse<List<ApplicationJobtestQueryDTO>>> getApplicationJobtestList() {
        List<ApplicationJobtestQueryDTO> applicationJobtestList = applicationJobtestQueryService.getAllApplicationJobTests();
        return ResponseEntity.status(ResponseCode.SUCCESS.getHttpStatus())
                .body(CustomApiResponse.of(ResponseCode.SUCCESS, applicationJobtestList));
    }
}
