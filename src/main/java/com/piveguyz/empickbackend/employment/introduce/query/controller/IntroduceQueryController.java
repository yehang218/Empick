package com.piveguyz.empickbackend.employment.introduce.query.controller;

import com.piveguyz.empickbackend.employment.introduce.query.dto.IntroduceQueryDTO;
import com.piveguyz.empickbackend.employment.introduce.query.service.IntroduceQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
    @RequestMapping("/api/introduce")
    @RequiredArgsConstructor
    public class IntroduceQueryController {

        private final IntroduceQueryService introduceQueryService;

        @GetMapping
        public ResponseEntity<List<IntroduceQueryDTO>> getAllIntroduce() {
            List<IntroduceQueryDTO> introduce = introduceQueryService.findAllIntroduce();
            return ResponseEntity.ok(introduce);
        }
    }


