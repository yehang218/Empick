package com.piveguyz.empickbackend.approvals.approval.query.service;

import com.piveguyz.empickbackend.approvals.approval.query.dto.ApprovalLineQueryDTO;

import java.util.List;

public interface ApprovalLineService {
    List<ApprovalLineQueryDTO> getApprovalLinePreview(Integer categoryId, Integer writerId);
}
