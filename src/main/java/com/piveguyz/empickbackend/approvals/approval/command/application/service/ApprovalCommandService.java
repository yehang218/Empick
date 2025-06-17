package com.piveguyz.empickbackend.approvals.approval.command.application.service;

import com.piveguyz.empickbackend.approvals.approval.command.application.dto.*;

import java.util.List;

public interface ApprovalCommandService {
    Integer createApproval(CreateApprovalCommandDTO dto);

    void approve(ApproveRequestDTO dto);

    void reject(RejectRequestDTO dto);

    void approveCancel(ApproveCancelRequestDTO dto);

}
