package com.piveguyz.empickbackend.approvals.approvalContent.command.application.service;


import com.piveguyz.empickbackend.approvals.approvalContent.command.application.dto.ApprovalContentCommandDTO;

import java.time.LocalDateTime;

public interface ApprovalContentCommandService {
    ApprovalContentCommandDTO create(ApprovalContentCommandDTO dto);

    ApprovalContentCommandDTO update(Integer id, ApprovalContentCommandDTO dto);

    ApprovalContentCommandDTO delete(Integer id);

    ApprovalContentCommandDTO updateDateTime(Integer id, LocalDateTime datetime);

    ApprovalContentCommandDTO updateAddress(Integer id, String address);
}
