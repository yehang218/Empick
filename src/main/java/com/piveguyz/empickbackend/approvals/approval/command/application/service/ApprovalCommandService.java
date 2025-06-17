package com.piveguyz.empickbackend.approvals.approval.command.application.service;

import com.piveguyz.empickbackend.approvals.approval.command.application.dto.*;

import java.util.List;

public interface ApprovalCommandService {
    Integer createApproval(CreateApprovalCommandDTO dto);

    void approve(int approvalId, ApproveRequestDTO dto);

    void reject(int approvalId, RejectRequestDTO dto);


}
