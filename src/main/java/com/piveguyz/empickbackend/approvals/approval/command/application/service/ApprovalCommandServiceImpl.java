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
import jakarta.persistence.EntityNotFoundException;
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
        // 결재자가 없는 경우
        if (dto.getApprovers() == null || dto.getApprovers().isEmpty()) {
            throw new BusinessException(ResponseCode.APPROVAL_NO_APPROVER);
        }
        // 결재 항목이 없는 경우
        if (dto.getContents() == null || dto.getContents().isEmpty()) {
            throw new BusinessException(ResponseCode.APPROVAL_CONTENT_ITEM_MISSING);
        }

        // 결재 순서(order) 중복 검사 및 map 생성
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
            // 혹시라도 Collectors.toMap에서 발생하는 중복 예외(위에서 던짐)
            throw new BusinessException(ResponseCode.APPROVAL_DUPLICATE_APPROVER_ORDER);
        }

        // 결재 유형별 항목 유효성 체크
        for (CreateApprovalCommandDTO.ApprovalContentDTO contentDTO : dto.getContents()) {
            boolean valid = approvalCategoryItemRepository
                    .existsByIdAndCategoryId(contentDTO.getItemId(), dto.getCategoryId());
            if (!valid) {
                throw new BusinessException(ResponseCode.APPROVAL_CATEGORY_ITEM_MISMATCH);
            }
        }

        // Entity 생성
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

        List<ApprovalContentEntity> contents = dto.getContents().stream()
                .map(c -> ApprovalContentEntity.builder()
                        .approvalId(approval.getId())
                        .itemId(c.getItemId())
                        .content(c.getContent())
                        .build()
                ).toList();

        approvalContentRepository.saveAll(contents);

        return approval.getId();
    }


    // 결재 승인
    @Transactional
    public void approve(ApproveRequestDTO dto) {
        ApprovalEntity approval = approvalRepository.findById(dto.getApprovalId())
                .orElseThrow(() -> new BusinessException(ResponseCode.APPROVAL_NOT_FOUND));

        Integer approverId = dto.getApproverId();
        LocalDateTime now = LocalDateTime.now();

        // 이미 완료/반려 상태면 에러
        if (approval.getStatus() == 1)
            throw new BusinessException(ResponseCode.APPROVAL_ALREADY_APPROVED);
        if (approval.getStatus() == -1)
            throw new BusinessException(ResponseCode.APPROVAL_ALREADY_REJECTED);

        // 결재 순서 체크 (1~4)
        // "1차 결재자 차례" → firstApprovedAt == null
        if (approval.getFirstApproverId() != null && approval.getFirstApprovedAt() == null) {
            if (!approverId.equals(approval.getFirstApproverId()))
                throw new BusinessException(ResponseCode.APPROVAL_APPROVER_NOT_FOUND);
            approval.setFirstApprovedAt(now);
            // 다음 결재자 없으면 완료 처리
            if (approval.getSecondApproverId() == null) {
                approval.setStatus(1);
            } else {
                approval.setStatus(0);
            }
            approvalRepository.save(approval);
            return;
        }
        // "2차 결재자 차례" → firstApprovedAt != null && secondApprovedAt == null
        if (approval.getSecondApproverId() != null && approval.getFirstApprovedAt() != null && approval.getSecondApprovedAt() == null) {
            if (!approverId.equals(approval.getSecondApproverId()))
                throw new BusinessException(ResponseCode.APPROVAL_APPROVER_NOT_FOUND);
            approval.setSecondApprovedAt(now);
            if (approval.getThirdApproverId() == null) {
                approval.setStatus(1);
            } else {
                approval.setStatus(0);
            }
            approvalRepository.save(approval);
            return;
        }
        // "3차 결재자 차례"
        if (approval.getThirdApproverId() != null && approval.getSecondApprovedAt() != null && approval.getThirdApprovedAt() == null) {
            if (!approverId.equals(approval.getThirdApproverId()))
                throw new BusinessException(ResponseCode.APPROVAL_APPROVER_NOT_FOUND);
            approval.setThirdApprovedAt(now);
            if (approval.getFourthApproverId() == null) {
                approval.setStatus(1);
            } else {
                approval.setStatus(0);
            }
            approvalRepository.save(approval);
            return;
        }
        // "4차 결재자 차례"
        if (approval.getFourthApproverId() != null && approval.getThirdApprovedAt() != null && approval.getFourthApprovedAt() == null) {
            if (!approverId.equals(approval.getFourthApproverId()))
                throw new BusinessException(ResponseCode.APPROVAL_APPROVER_NOT_FOUND);
            approval.setFourthApprovedAt(now);
            approval.setStatus(1); // 무조건 완료
            approvalRepository.save(approval);
            return;
        }

        // 위 경우에 모두 해당하지 않으면
        throw new BusinessException(ResponseCode.APPROVAL_NOT_YOUR_TURN); // "결재 차례가 아닙니다"
    }

    // 결재 반려
    @Transactional
    public void reject(RejectRequestDTO dto) {
        ApprovalEntity approval = approvalRepository.findById(dto.getApprovalId())
                .orElseThrow(() -> new BusinessException(ResponseCode.APPROVAL_NOT_FOUND));

        // 이미 승인 또는 반려된 결재는 반려 불가
        if (approval.getStatus() == 1) {
            throw new BusinessException(ResponseCode.APPROVAL_ALREADY_APPROVED);
        }
        if (approval.getStatus() == -1) {
            throw new BusinessException(ResponseCode.APPROVAL_ALREADY_REJECTED);
        }

        // 반려 사유 (필드가 비어 있거나 null인 경우)
//        if (dto.getRejectReason() == null || dto.getRejectReason().isBlank()) {
//            throw new BusinessException(ResponseCode.APPROVAL_REJECT_REASON_REQUIRED);
//        }

        // 결재 차례(=승인 차례)만 반려 가능
        Integer rejectorId = dto.getApproverId();

        // 1차 결재자
        if (approval.getFirstApproverId() != null && approval.getFirstApprovedAt() == null) {
            if (!rejectorId.equals(approval.getFirstApproverId()))
                throw new BusinessException(ResponseCode.APPROVAL_NOT_YOUR_TURN);
        }
        // 2차 결재자
        else if (approval.getSecondApproverId() != null && approval.getFirstApprovedAt() != null && approval.getSecondApprovedAt() == null) {
            if (!rejectorId.equals(approval.getSecondApproverId()))
                throw new BusinessException(ResponseCode.APPROVAL_NOT_YOUR_TURN);
        }
        // 3차 결재자
        else if (approval.getThirdApproverId() != null && approval.getSecondApprovedAt() != null && approval.getThirdApprovedAt() == null) {
            if (!rejectorId.equals(approval.getThirdApproverId()))
                throw new BusinessException(ResponseCode.APPROVAL_NOT_YOUR_TURN);
        }
        // 4차 결재자
        else if (approval.getFourthApproverId() != null && approval.getThirdApprovedAt() != null && approval.getFourthApprovedAt() == null) {
            if (!rejectorId.equals(approval.getFourthApproverId()))
                throw new BusinessException(ResponseCode.APPROVAL_NOT_YOUR_TURN);
        }
        // 누구 차례도 아니면
        else {
            throw new BusinessException(ResponseCode.APPROVAL_NOT_YOUR_TURN);
        }

        // 상태 반려 처리
        approval.setStatus(-1);

        approvalRepository.save(approval);
    }

    // 취소 결재 승인
    @Transactional
    public void approveCancel(ApproveCancelRequestDTO dto) {
        ApprovalEntity cancelApproval = approvalRepository.findById(dto.getApprovalId())
                .orElseThrow(() -> new BusinessException(ResponseCode.APPROVAL_NOT_FOUND));

        // 이미 승인/반려된 취소 결재는 승인 불가
        if (cancelApproval.getStatus() == 1) {
            throw new BusinessException(ResponseCode.APPROVAL_ALREADY_APPROVED);
        }
        if (cancelApproval.getStatus() == -1) {
            throw new BusinessException(ResponseCode.APPROVAL_ALREADY_REJECTED);
        }

        // 취소 대상 결재문서 체크
        ApprovalEntity targetApproval = approvalRepository.findById(dto.getTargetApprovalId())
                .orElseThrow(() -> new BusinessException(ResponseCode.APPROVAL_CANCEL_TARGET_NOT_FOUND));
        // 이미 취소된 문서는 재취소 불가
        if (targetApproval.getStatus() == -2) {
            throw new BusinessException(ResponseCode.APPROVAL_ALREADY_PROCESSED);
        }

        // 상태 변경
        cancelApproval.setStatus(1); // 취소결재 승인
        targetApproval.setStatus(-2); // 대상 문서 상태를 '취소됨'으로

        approvalRepository.save(cancelApproval);
        approvalRepository.save(targetApproval);
    }
}
