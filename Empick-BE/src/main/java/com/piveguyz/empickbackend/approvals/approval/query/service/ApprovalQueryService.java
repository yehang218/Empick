package com.piveguyz.empickbackend.approvals.approval.query.service;

import com.piveguyz.empickbackend.approvals.approval.query.dto.ApprovalQueryDTO;
import com.piveguyz.empickbackend.approvals.approval.query.dto.ApprovalReceivedDetailQueryDTO;
import com.piveguyz.empickbackend.approvals.approval.query.dto.ApprovalReceivedQueryDTO;
import com.piveguyz.empickbackend.approvals.approval.query.dto.ApprovalRequestedListQueryDTO;

import java.util.List;

public interface ApprovalQueryService {

	List<ApprovalQueryDTO> findAll();

	ApprovalQueryDTO findById(Integer id);

	List<ApprovalQueryDTO> findByCategoryId(Integer categoryId);

	List<ApprovalQueryDTO> findByWriterId(Integer writerId);

	List<ApprovalReceivedQueryDTO> findReceivedApprovals(Integer memberId);

	List<ApprovalRequestedListQueryDTO> findRequestedApprovals(Integer memberId);
}
