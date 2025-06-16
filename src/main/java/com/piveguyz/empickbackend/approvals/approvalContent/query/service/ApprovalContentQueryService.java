package com.piveguyz.empickbackend.approvals.approvalContent.query.service;

import com.piveguyz.empickbackend.approvals.approvalContent.query.dto.ApprovalContentQueryDTO;

import java.util.List;

public interface ApprovalContentQueryService {

	List<ApprovalContentQueryDTO> findAll();

	ApprovalContentQueryDTO findById(Integer id);

	List<ApprovalContentQueryDTO> findByApprovalId(Integer approvalId);
}
