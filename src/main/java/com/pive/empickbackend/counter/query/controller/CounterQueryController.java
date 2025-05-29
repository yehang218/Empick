// counter/query/controller/CounterQueryController.java
package com.pive.empickbackend.counter.query.controller;

import com.pive.empickbackend.counter.query.service.CounterQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Counter", description = "증가, 감소, 카운트값")
@RestController
@RequestMapping("/api/counter")
@RequiredArgsConstructor
public class CounterQueryController {

    private final CounterQueryService counterService;

    @Operation(
            summary = "카운트 값 요청",
            description = """
        - 여기에 설명을 하면됨
        """
    )
    @GetMapping
    public int getCount() {
        return counterService.get();
    }

    @Operation(
            summary = "증가 요청",
            description = """
        - 여기에 설명을 하면됨
        """
    )
    @PostMapping("/increment")
    public int increment() {
        return counterService.increment();
    }

    @Operation(
            summary = "감소 요청",
            description = """
        - 여기에 설명을 하면됨
        """
    )
    @PostMapping("/decrement")
    public int decrement() {
        return counterService.decrement();
    }
}
