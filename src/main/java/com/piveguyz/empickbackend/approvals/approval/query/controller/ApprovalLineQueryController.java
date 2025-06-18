package com.piveguyz.empickbackend.approvals.approval.query.controller;

import com.piveguyz.empickbackend.approvals.approval.query.dto.ApprovalLineQueryDTO;
import com.piveguyz.empickbackend.approvals.approval.query.mapper.ApprovalQueryMapper;
import com.piveguyz.empickbackend.approvals.approval.query.service.ApprovalLineService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "결재 문서 API", description = "결재 문서 관련 전체 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/approval/categories")
public class ApprovalLineQueryController {

    private final ApprovalLineService approvalLineService;

    // 결재라인 미리보기
    @GetMapping("/{categoryId}/lines")
    public List<ApprovalLineQueryDTO> getApprovalLinePreview(
            @PathVariable Integer categoryId,
            @RequestParam Integer writerId
    ) {
        return approvalLineService.getApprovalLinePreview(categoryId, writerId);
    }
}
