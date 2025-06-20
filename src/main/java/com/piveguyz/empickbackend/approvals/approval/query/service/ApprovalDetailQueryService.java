package com.piveguyz.empickbackend.approvals.approval.query.service;

import com.piveguyz.empickbackend.approvals.approval.query.dto.ApprovalReceivedDetailQueryDTO;

public interface ApprovalDetailQueryService {
	ApprovalReceivedDetailQueryDTO getReceivedApprovalDetail(Integer approvalId, Integer memberId);
}
