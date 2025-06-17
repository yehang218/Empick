package com.piveguyz.empickbackend.approvals.approval.query.service;

import com.piveguyz.empickbackend.approvals.approval.query.dto.ApprovalQueryDTO;

import java.util.List;

public interface ApprovalQueryService {

	List<ApprovalQueryDTO> findAll();

	ApprovalQueryDTO findById(Integer id);

	List<ApprovalQueryDTO> findByCategoryId(Integer categoryId);

	List<ApprovalQueryDTO> findByWriterId(Integer writerId);

	List<ApprovalQueryDTO> findReceivedApprovals(Integer memberId);
}
