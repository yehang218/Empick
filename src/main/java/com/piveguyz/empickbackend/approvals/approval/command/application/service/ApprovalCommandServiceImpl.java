package com.piveguyz.empickbackend.approvals.approval.command.application.service;

import com.piveguyz.empickbackend.approvals.approval.command.application.dto.ApprovalCommandResponseDTO;
import com.piveguyz.empickbackend.approvals.approval.command.application.dto.CreateApprovalCommandDTO;
import com.piveguyz.empickbackend.approvals.approval.command.domain.aggregate.ApprovalContentEntity;
import com.piveguyz.empickbackend.approvals.approval.command.domain.aggregate.ApprovalEntity;
import com.piveguyz.empickbackend.approvals.approval.command.domain.repository.ApprovalCategoryItemRepository;
import com.piveguyz.empickbackend.approvals.approval.command.domain.repository.ApprovalContentRepository;
import com.piveguyz.empickbackend.approvals.approval.command.domain.repository.ApprovalRepository;
import com.piveguyz.empickbackend.orgstructure.member.command.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        Map<Integer, Integer> approverMap = dto.getApprovers().stream()
                .collect(Collectors.toMap(CreateApprovalCommandDTO.ApproverDTO::getOrder, CreateApprovalCommandDTO.ApproverDTO::getMemberId));

        for (CreateApprovalCommandDTO.ApprovalContentDTO contentDTO : dto.getContents()) {
            boolean valid = approvalCategoryItemRepository
                    .existsByIdAndCategoryId(contentDTO.getItemId(), dto.getCategoryId());
            if (!valid) {
                throw new IllegalArgumentException("항목 ID " + contentDTO.getItemId() + "은 해당 결재 유형에 속하지 않습니다.");
            }
        }

        ApprovalEntity approval = ApprovalEntity.builder()
                .categoryId(dto.getCategoryId())
                .writerId(dto.getWriterId())
                .firstApproverId(approverMap.get(1))
                .secondApproverId(approverMap.get(2))
                .thirdApproverId(approverMap.get(3))
                .fourthApproverId(approverMap.get(4))
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
}
