package com.piveguyz.empickbackend.approvals.approval.command.application.service;

import com.piveguyz.empickbackend.approvals.approval.command.application.dto.*;
import com.piveguyz.empickbackend.approvals.approval.command.domain.aggregate.ApprovalContentEntity;
import com.piveguyz.empickbackend.approvals.approval.command.domain.aggregate.ApprovalEntity;
import com.piveguyz.empickbackend.approvals.approval.command.domain.aggregate.ApprovalLineEntity;
import com.piveguyz.empickbackend.approvals.approval.command.domain.repository.ApprovalCategoryItemRepository;
import com.piveguyz.empickbackend.approvals.approval.command.domain.repository.ApprovalContentRepository;
import com.piveguyz.empickbackend.approvals.approval.command.domain.repository.ApprovalLineRepository;
import com.piveguyz.empickbackend.approvals.approval.command.domain.repository.ApprovalRepository;
import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.member.command.domain.aggregate.MemberEntity;
import com.piveguyz.empickbackend.orgstructure.member.command.domain.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApprovalCommandServiceImpl implements ApprovalCommandService {
    private final MemberRepository memberRepository;
    private final ApprovalRepository approvalRepository;
    private final ApprovalCategoryItemRepository approvalCategoryItemRepository;
    private final ApprovalContentRepository approvalContentRepository;
    private final ApprovalLineRepository approvalLineRepository;

    @Override
    public Integer createApproval(CreateApprovalCommandDTO dto) {
        if (dto.getContents() == null || dto.getContents().isEmpty()) {
            throw new BusinessException(ResponseCode.APPROVAL_CONTENT_ITEM_MISSING);
        }

        // 결재 라인 조회 (approval_line 테이블)
        List<ApprovalLineEntity> approvalLine = approvalLineRepository
                .findByApprovalCategoryIdOrderByStepOrderAsc(dto.getCategoryId());

        if (approvalLine.isEmpty()) {
            throw new BusinessException(ResponseCode.APPROVAL_LINE_NOT_FOUND);
        }

        // 기안자의 부서 조회
        MemberEntity writer = memberRepository.findById(dto.getWriterId())
                .orElseThrow(() -> new BusinessException(ResponseCode.MEMBER_NOT_FOUND));
        Integer deptId = writer.getDepartmentId(); // 부서 기준 할당

        // 결재자 자동 할당
        Map<Integer, Integer> approverMap = new HashMap<>();
        for (ApprovalLineEntity line : approvalLine) {
            // 해당 부서에서 지정된 직책의 멤버 찾기 (없으면 예외)
            Integer step = line.getStepOrder();
            Integer positionId = line.getPositionId();

            Optional<MemberEntity> approverOpt = memberRepository
                    .findFirstByDeptIdAndPositionId(deptId, positionId);

            Integer memberId = approverOpt
                    .orElseThrow(() -> new BusinessException(ResponseCode.APPROVAL_APPROVER_NOT_FOUND))
                    .getId();

            approverMap.put(step, memberId);
        }

        ApprovalEntity approval = ApprovalEntity.builder()
                .categoryId(dto.getCategoryId())
                .writerId(dto.getWriterId())
                .firstApproverId(approverMap.get(1))
                .secondApproverId(approverMap.get(2))
                .thirdApproverId(approverMap.get(3))
                .fourthApproverId(approverMap.get(4))
                .approvalId(dto.getApprovalId())
                .build();

        approvalRepository.save(approval);

        // 결재문서 내용 저장
        List<ApprovalContentEntity> contents = dto.getContents().stream()
                .map(c -> ApprovalContentEntity.builder()
                        .approvalId(approval.getId())
                        .itemId(c.getItemId())
                        .content(c.getContent())
                        .build())
                .toList();
        approvalContentRepository.saveAll(contents);

        return approval.getId();
    }

    // ================== 공통 유틸 ==================

    // 이미 승인 또는 반려, 취소된 결재문서면 예외 throw
    private void validateNotProcessed(ApprovalEntity approval) {
        if (approval.getStatus() == 1)
            throw new BusinessException(ResponseCode.APPROVAL_ALREADY_APPROVED);
        if (approval.getStatus() == -1)
            throw new BusinessException(ResponseCode.APPROVAL_ALREADY_REJECTED);
        if(approval.getStatus() == -2)
            throw new BusinessException(ResponseCode.APPROVAL_ALREADY_CANCELED);
    }

    // 현재 결재 차례의 ApproverId 리턴 (없으면 예외)
    private Integer validateAndGetCurrentApproverId(ApprovalEntity approval) {
        if (approval.getFirstApproverId() != null && approval.getFirstApprovedAt() == null) {
            return approval.getFirstApproverId();
        }
        if (approval.getSecondApproverId() != null && approval.getFirstApprovedAt() != null && approval.getSecondApprovedAt() == null) {
            return approval.getSecondApproverId();
        }
        if (approval.getThirdApproverId() != null && approval.getSecondApprovedAt() != null && approval.getThirdApprovedAt() == null) {
            return approval.getThirdApproverId();
        }
        if (approval.getFourthApproverId() != null && approval.getThirdApprovedAt() != null && approval.getFourthApprovedAt() == null) {
            return approval.getFourthApproverId();
        }
        throw new BusinessException(ResponseCode.APPROVAL_NOT_YOUR_TURN);
    }

    // 결재 승인일시를 올바른 필드에 set
    private void setApprovedAt(ApprovalEntity approval, LocalDateTime now) {
        if (approval.getFirstApproverId() != null && approval.getFirstApprovedAt() == null) {
            approval.setFirstApprovedAt(now);
        } else if (approval.getSecondApproverId() != null && approval.getFirstApprovedAt() != null && approval.getSecondApprovedAt() == null) {
            approval.setSecondApprovedAt(now);
        } else if (approval.getThirdApproverId() != null && approval.getSecondApprovedAt() != null && approval.getThirdApprovedAt() == null) {
            approval.setThirdApprovedAt(now);
        } else if (approval.getFourthApproverId() != null && approval.getThirdApprovedAt() != null && approval.getFourthApprovedAt() == null) {
            approval.setFourthApprovedAt(now);
        }
    }

    // 마지막 결재자인지 판별
    private boolean isFinalApprover(ApprovalEntity approval) {
        return (approval.getFourthApproverId() != null && approval.getFourthApprovedAt() != null)
                || (approval.getThirdApproverId() != null && approval.getThirdApprovedAt() != null && approval.getFourthApproverId() == null)
                || (approval.getSecondApproverId() != null && approval.getSecondApprovedAt() != null && approval.getThirdApproverId() == null)
                || (approval.getFirstApproverId() != null && approval.getFirstApprovedAt() != null && approval.getSecondApproverId() == null);
    }

    // ================== 결재 승인 ==================

    @Override
    @Transactional
    public void approve(int approvalId, ApproveRequestDTO dto) {
        ApprovalEntity approval = approvalRepository.findById(approvalId)
                .orElseThrow(() -> new BusinessException(ResponseCode.APPROVAL_NOT_FOUND));

        validateNotProcessed(approval);

        Integer currentApproverId = validateAndGetCurrentApproverId(approval);

        if (!memberRepository.existsById(dto.getApproverId())) {
            throw new BusinessException(ResponseCode.MEMBER_NOT_FOUND);
        }

        if (!dto.getApproverId().equals(currentApproverId)) {
            throw new BusinessException(ResponseCode.APPROVAL_NOT_YOUR_TURN);
        }

        LocalDateTime now = LocalDateTime.now();
        setApprovedAt(approval, now);

        // 마지막 결재자인 경우 상태 완료
        if (isFinalApprover(approval)) {

            // 만약 결재 취소 요청이었다면
            Integer targetApprovalId = approval.getApprovalId();
            if(targetApprovalId != null) approveCancel(targetApprovalId);
            approval.setStatus(1);
        } else {
            approval.setStatus(0); // 계속 진행중
        }

        approvalRepository.save(approval);
    }

    // ================== 결재 반려 ==================

    @Override
    @Transactional
    public void reject(int approvalId, RejectRequestDTO dto) {
        ApprovalEntity approval = approvalRepository.findById(approvalId)
                .orElseThrow(() -> new BusinessException(ResponseCode.APPROVAL_NOT_FOUND));

        validateNotProcessed(approval);

        Integer currentApproverId = validateAndGetCurrentApproverId(approval);

        if (!dto.getApproverId().equals(currentApproverId)) {
            throw new BusinessException(ResponseCode.APPROVAL_NOT_YOUR_TURN);
        }

        //  🚩 반려 사유/일시 등 기록

        approval.setStatus(-1);
        approvalRepository.save(approval);
    }

    // ================== 취소 결재 승인 ==================
    private void approveCancel(Integer targetApprovalId) {
        ApprovalEntity targetApproval = approvalRepository.findById(targetApprovalId)
                .orElseThrow(() -> new BusinessException(ResponseCode.APPROVAL_CANCEL_TARGET_NOT_FOUND));
        if (targetApproval.getStatus() == -2) {
            throw new BusinessException(ResponseCode.APPROVAL_ALREADY_PROCESSED);
        }

        targetApproval.setStatus(-2);  // 대상 문서 상태 '취소됨"으로 변경

        approvalRepository.save(targetApproval);
    }
}
