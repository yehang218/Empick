// calculator/query/controller/CalculatorQueryController.java
package com.piveguyz.empickbackend.calculator.query.controller;

import com.piveguyz.empickbackend.calculator.query.dto.AddRequestDto;
import com.piveguyz.empickbackend.calculator.query.dto.SubtractRequestDto;
import com.piveguyz.empickbackend.calculator.query.service.CalculatorQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "계산기 예제 API ", description = "덧샘, 뺄샘")
@RestController
@RequestMapping("/api/calculator")
@RequiredArgsConstructor
@Slf4j
public class CalculatorQueryController {

    private final CalculatorQueryService calculatorService;

    @Operation(
            summary = "더하기 기능",
            description = """
        이 엔드포인트로 값들을 보내면 두 값을 더해서 반환한다. 
        - 여기에 설명을 하면됨
        - 200 코드 : 지정된 값의 형태로 반환
        """
    )
    @PostMapping("/add")
    public int add(@RequestBody AddRequestDto dto) {
        return calculatorService.add(dto);
    }

    @Operation(
            summary = "빼기 기능",
            description = """
        이 엔드포인트로 값들을 보내면 두 값을 빼서 반환한다. 
        - 여기에 설명을 하면됨
        - 200 코드 : 지정된 값의 형태로 반환
        """
    )
    @PostMapping("/subtract")
    public int subtract(@RequestBody SubtractRequestDto dto) {
        return calculatorService.subtract(dto);
    }
}
