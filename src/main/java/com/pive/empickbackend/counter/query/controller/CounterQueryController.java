// counter/query/controller/CounterQueryController.java
package com.pive.empickbackend.counter.query.controller;

import com.pive.empickbackend.counter.query.service.CounterQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/counter")
@RequiredArgsConstructor
public class CounterQueryController {

    private final CounterQueryService counterService;

    @GetMapping
    public int getCount() {
        return counterService.get();
    }

    @PostMapping("/increment")
    public int increment() {
        return counterService.increment();
    }

    @PostMapping("/decrement")
    public int decrement() {
        return counterService.decrement();
    }
}
