// counter/query/service/CounterQueryService.java
package com.piveguyz.empickbackend.counter.query.service;

import org.springframework.stereotype.Service;

@Service
public class CounterQueryService {

    private int count = 0;

    public int get() {
        return count;
    }

    public int increment() {
        return ++count;
    }

    public int decrement() {
        return --count;
    }
}
