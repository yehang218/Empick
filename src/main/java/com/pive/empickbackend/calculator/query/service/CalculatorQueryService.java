// calculator/query/service/CalculatorQueryService.java
package com.pive.empickbackend.calculator.query.service;

import com.pive.empickbackend.calculator.query.dto.AddRequestDto;
import com.pive.empickbackend.calculator.query.dto.SubtractRequestDto;
import org.springframework.stereotype.Service;

@Service
public class CalculatorQueryService {

    public int add(AddRequestDto dto) {
        return dto.getA() + dto.getB();
    }

    public int subtract(SubtractRequestDto dto) {
        return dto.getA() - dto.getB();
    }
}
