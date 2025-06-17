package com.piveguyz.empickbackend.approvals.approval.command.application.service;

import com.piveguyz.empickbackend.approvals.approval.command.application.dto.*;
import com.piveguyz.empickbackend.approvals.approval.command.domain.aggregate.ApprovalContentEntity;
import com.piveguyz.empickbackend.approvals.approval.command.domain.aggregate.ApprovalEntity;
import com.piveguyz.empickbackend.approvals.approval.command.domain.repository.ApprovalCategoryItemRepository;
import com.piveguyz.empickbackend.approvals.approval.command.domain.repository.ApprovalContentRepository;
import com.piveguyz.empickbackend.approvals.approval.command.domain.repository.ApprovalRepository;
import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.member.command.domain.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApprovalCommandServiceImpl implements ApprovalCommandService {
    private final MemberRepository memberRepository;
    private final ApprovalRepository approvalRepository;
    private final ApprovalCategoryItemRepository approvalCategoryItemRepository;
    private final ApprovalContentRepository approvalContentRepository;

    @Override
    public Integer createApproval(CreateApprovalCommandDTO dto) {
        // 결재자/항목 필수 검증
        if (dto.getApprovers() == null || dto.getApprovers().isEmpty()) {
            throw new BusinessException(ResponseCode.APPROVAL_NO_APPROVER);
        }
        if (dto.getContents() == null || dto.getContents().isEmpty()) {
            throw new BusinessException(ResponseCode.APPROVAL_CONTENT_ITEM_MISSING);
        }

        // 결재 순서 중복 검사
        Map<Integer, Integer> approverMap;
        try {
            approverMap = dto.getApprovers().stream()
                    .collect(Collectors.toMap(
                            CreateApprovalCommandDTO.ApproverDTO::getOrder,
                            CreateApprovalCommandDTO.ApproverDTO::getMemberId,
                            (a, b) -> {
                                throw new BusinessException(ResponseCode.APPROVAL_DUPLICATE_APPROVER_ORDER);
                            }
                    ));
        } catch (IllegalStateException e) {
            throw new BusinessException(ResponseCode.APPROVAL_DUPLICATE_APPROVER_ORDER);
        }

        // 결재선의 모든 결재자가 member에 있는지
        for (Integer memberId : approverMap.values()) {
            if (memberId != null && !memberRepository.existsById(memberId)) {
                throw new BusinessException(ResponseCode.APPROVAL_APPROVER_NOT_FOUND);
            }
        }

        // 항목 유효성 체크
        for (CreateApprovalCommandDTO.ApprovalContentDTO contentDTO : dto.getContents()) {
            boolean valid = approvalCategoryItemRepository
                    .existsByIdAndCategoryId(contentDTO.getItemId(), dto.getCategoryId());
            if (!valid) {
                throw new BusinessException(ResponseCode.APPROVAL_CATEGORY_ITEM_MISMATCH);
            }
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

    // 이미 승인 또는 반려된 결재문서면 예외 throw
    private void validateNotProcessed(ApprovalEntity approval) {
        if (approval.getStatus() == 1)
            throw new BusinessException(ResponseCode.APPROVAL_ALREADY_APPROVED);
        if (approval.getStatus() == -1)
            throw new BusinessException(ResponseCode.APPROVAL_ALREADY_REJECTED);
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
    public void approve(ApproveRequestDTO dto) {
        ApprovalEntity approval = approvalRepository.findById(dto.getApprovalId())
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
            approval.setStatus(1);
        } else {
            approval.setStatus(0); // 계속 진행중
        }

        approvalRepository.save(approval);
    }

    // ================== 결재 반려 ==================

    @Override
    @Transactional
    public void reject(RejectRequestDTO dto) {
        ApprovalEntity approval = approvalRepository.findById(dto.getApprovalId())
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

    @Override
    @Transactional
    public void approveCancel(ApproveCancelRequestDTO dto) {
        ApprovalEntity cancelApproval = approvalRepository.findById(dto.getApprovalId())
                .orElseThrow(() -> new BusinessException(ResponseCode.APPROVAL_NOT_FOUND));

        validateNotProcessed(cancelApproval);

        ApprovalEntity targetApproval = approvalRepository.findById(dto.getTargetApprovalId())
                .orElseThrow(() -> new BusinessException(ResponseCode.APPROVAL_CANCEL_TARGET_NOT_FOUND));
        if (targetApproval.getStatus() == -2) {
            throw new BusinessException(ResponseCode.APPROVAL_ALREADY_PROCESSED);
        }

        cancelApproval.setStatus(1);   // 취소결재 승인
        targetApproval.setStatus(-2);  // 대상 문서 '취소됨'

        approvalRepository.save(cancelApproval);
        approvalRepository.save(targetApproval);
    }
}
