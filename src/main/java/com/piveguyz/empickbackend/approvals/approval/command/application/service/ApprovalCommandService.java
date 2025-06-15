package com.piveguyz.empickbackend.approvals.approval.command.application.service;


import com.piveguyz.empickbackend.approvals.approval.command.application.dto.ApprovalCommandDTO;

import java.time.LocalDateTime;

public interface ApprovalCommandService {
    ApprovalCommandDTO create(ApprovalCommandDTO dto);

    ApprovalCommandDTO update(Integer id, ApprovalCommandDTO dto);

    ApprovalCommandDTO delete(Integer id);

    ApprovalCommandDTO updateDateTime(Integer id, LocalDateTime datetime);

    ApprovalCommandDTO updateAddress(Integer id, String address);
}
