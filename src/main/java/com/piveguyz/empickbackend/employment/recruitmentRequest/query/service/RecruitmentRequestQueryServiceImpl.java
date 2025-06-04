package com.piveguyz.empickbackend.employment.recruitmentRequest.query.service;

import org.springframework.stereotype.Service;

import com.piveguyz.empickbackend.employment.recruitmentRequest.command.domain.repository.RecruitmentRequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitmentRequestQueryServiceImpl {
	private final RecruitmentRequestRepository recruitmentRequestRepository;

}
