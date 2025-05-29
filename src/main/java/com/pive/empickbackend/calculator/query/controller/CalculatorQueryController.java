// calculator/query/controller/CalculatorQueryController.java
package com.pive.empickbackend.calculator.query.controller;

import com.pive.empickbackend.calculator.query.dto.AddRequestDto;
import com.pive.empickbackend.calculator.query.dto.SubtractRequestDto;
import com.pive.empickbackend.calculator.query.service.CalculatorQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calculator")
@RequiredArgsConstructor
public class CalculatorQueryController {

    private final CalculatorQueryService calculatorService;

    @PostMapping("/add")
    public int add(@RequestBody AddRequestDto dto) {
        return calculatorService.add(dto);
    }

    @PostMapping("/subtract")
    public int subtract(@RequestBody SubtractRequestDto dto) {
        return calculatorService.subtract(dto);
    }
}
