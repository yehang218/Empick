package com.piveguyz.empickbackend.approvals.approval.query.service;

import com.piveguyz.empickbackend.approvals.approval.query.dto.ApprovalLineQueryDTO;
import com.piveguyz.empickbackend.approvals.approval.query.mapper.ApprovalQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalLineServiceImpl implements ApprovalLineService{

    private final ApprovalQueryMapper approvalQueryMapper;

    // 해당 유형에 해당하는 결재 라인 조회
    @Override
    public List<ApprovalLineQueryDTO> getApprovalLinePreview(Integer categoryId, Integer writerId) {
        return approvalQueryMapper.selectApprovalLinePreview(categoryId, writerId);
    }
}
