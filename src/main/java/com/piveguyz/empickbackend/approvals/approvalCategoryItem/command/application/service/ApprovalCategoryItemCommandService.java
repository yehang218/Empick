package com.piveguyz.empickbackend.approvals.approvalCategoryItem.command.application.service;


import com.piveguyz.empickbackend.approvals.approvalCategoryItem.command.application.dto.ApprovalCategoryItemCommandDTO;

import java.time.LocalDateTime;

public interface ApprovalCategoryItemCommandService {
    ApprovalCategoryItemCommandDTO create(ApprovalCategoryItemCommandDTO dto);

    ApprovalCategoryItemCommandDTO update(Integer id, ApprovalCategoryItemCommandDTO dto);

    ApprovalCategoryItemCommandDTO delete(Integer id);

    ApprovalCategoryItemCommandDTO updateDateTime(Integer id, LocalDateTime datetime);

    ApprovalCategoryItemCommandDTO updateAddress(Integer id, String address);
}
