package com.piveguyz.empickbackend.employment.introduce.query.controller;

import com.piveguyz.empickbackend.employment.introduce.query.dto.IntroduceQueryDTO;
import com.piveguyz.empickbackend.employment.introduce.query.service.IntroduceQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "자기소개 조회 API", description = "자기소개 관련 조회 기능을 제공합니다.")
@RestController
@RequestMapping("/api/v1/introduce")
@RequiredArgsConstructor
public class IntroduceQueryController {

    private final IntroduceQueryService introduceQueryService;

    @Operation(summary = "전체 자기소개 조회", description = "모든 자기소개 항목을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<IntroduceQueryDTO>> getAllIntroduce() {
        List<IntroduceQueryDTO> introduce = introduceQueryService.findAllIntroduce();
        return ResponseEntity.ok(introduce);
    }
}
