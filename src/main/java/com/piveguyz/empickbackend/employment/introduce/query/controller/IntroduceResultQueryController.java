package com.piveguyz.empickbackend.employment.applicant.query.controller;



import com.piveguyz.empickbackend.employment.introduce.query.dto.IntroduceResultQueryDTO;
import com.piveguyz.empickbackend.employment.introduce.query.service.IntroduceQueryService;
import com.piveguyz.empickbackend.employment.introduce.query.service.IntroduceResultQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/introduce-result")
@Tag(name = "자기소개서 평가 결과", description = "자소서 평가 결과 API")
public class IntroduceResultQueryController {

    private final IntroduceResultQueryService introduceResultQueryService;

    @Operation(summary = "자기소개서 평가 결과 전체 조회", description = "introduce_rating_result 전체 조회 API")
    @GetMapping
    public ResponseEntity<List<IntroduceResultQueryDTO>> getAllIntroduceResults() {
        List<IntroduceResultQueryDTO> result = introduceResultQueryService.findAllIntroduceResult();
        return ResponseEntity.ok(result);
    }
}
