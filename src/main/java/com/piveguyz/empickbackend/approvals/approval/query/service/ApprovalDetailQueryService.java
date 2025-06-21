package com.piveguyz.empickbackend.approvals.approval.query.service;

import com.piveguyz.empickbackend.approvals.approval.query.dto.ApprovalReceivedDetailQueryDTO;
import com.piveguyz.empickbackend.approvals.approval.query.dto.ApprovalRequestedDetailQueryDTO;

public interface ApprovalDetailQueryService {
	ApprovalReceivedDetailQueryDTO getReceivedApprovalDetail(Integer approvalId, Integer memberId);

	ApprovalRequestedDetailQueryDTO getRequestedApprovalDetail(Integer approvalId);
}
