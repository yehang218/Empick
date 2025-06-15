package com.piveguyz.empickbackend.approvals.approvalCategory.command.application.service;


import com.piveguyz.empickbackend.approvals.approvalCategory.command.application.dto.ApprovalCategoryCommandDTO;

import java.time.LocalDateTime;

public interface ApprovalCategoryCommandService {
    ApprovalCategoryCommandDTO create(ApprovalCategoryCommandDTO dto);

    ApprovalCategoryCommandDTO update(Integer id, ApprovalCategoryCommandDTO dto);

    ApprovalCategoryCommandDTO delete(Integer id);

    ApprovalCategoryCommandDTO updateDateTime(Integer id, LocalDateTime datetime);

    ApprovalCategoryCommandDTO updateAddress(Integer id, String address);
}
