package com.piveguyz.empickbackend.approvals.approval.command.application.service;

import com.piveguyz.empickbackend.approvals.approval.command.application.dto.ApprovalCommandResponseDTO;
import com.piveguyz.empickbackend.approvals.approval.command.application.dto.CreateApprovalCommandDTO;

import java.util.List;

public interface ApprovalCommandService {
    Integer createApproval(CreateApprovalCommandDTO dto);
}
