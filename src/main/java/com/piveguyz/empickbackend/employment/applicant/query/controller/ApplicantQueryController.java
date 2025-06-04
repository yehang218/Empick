package com.piveguyz.empickbackend.employment.applicant.query.controller;

import com.piveguyz.empickbackend.employment.applicant.query.dto.ApplicationQueryDTO;
import com.piveguyz.empickbackend.employment.applicant.query.service.ApplicantQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/applicant")
@RequiredArgsConstructor
public class ApplicantQueryController {

    private final ApplicantQueryService applicantQueryService;

    @GetMapping
    public ResponseEntity<List<ApplicationQueryDTO>> getAllApplicant() {
        return ResponseEntity.ok(applicantQueryService.findAllApplicant());

    }

}
