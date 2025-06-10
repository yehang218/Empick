package com.piveguyz.empickbackend.member.command.application.service;

import com.piveguyz.empickbackend.auth.facade.AuthFacade;
import com.piveguyz.empickbackend.common.constants.RoleCode;
import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.department.command.domain.repository.DeptRepository;
import com.piveguyz.empickbackend.job.command.domain.repository.JobRepository;
import com.piveguyz.empickbackend.member.command.application.dto.MemberEditProposalCommandDTO;
import com.piveguyz.empickbackend.member.command.application.dto.MemberEditRejectCommandDTO;
import com.piveguyz.empickbackend.member.command.domain.aggregate.MemberEditProposalEntity;
import com.piveguyz.empickbackend.member.command.domain.aggregate.MemberEntity;
import com.piveguyz.empickbackend.member.command.domain.enums.MemberEditStatus;
import com.piveguyz.empickbackend.member.command.domain.enums.MemberTargetField;
import com.piveguyz.empickbackend.member.command.domain.repository.MemberEditProposalRepository;
import com.piveguyz.empickbackend.member.command.domain.repository.MemberRepository;
import com.piveguyz.empickbackend.member.query.mapper.MemberEditProposalQueryMapper;
import com.piveguyz.empickbackend.position.command.domain.repository.PositionRepository;
import com.piveguyz.empickbackend.rank.command.domain.repository.RankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberEditProposalCommandServiceImpl implements MemberEditProposalCommandService {

    private final MemberEditProposalRepository memberEditProposalRepository;
    private final AuthFacade authFacade;
    private final MemberEditProposalQueryMapper proposalQueryMapper;
    private final MemberRepository memberRepository;

    private final DeptRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final JobRepository jobRepository;
    private final RankRepository rankRepository;

    @Override
    @Transactional
    public void createMemberEditRequest(MemberEditProposalCommandDTO dto) {
        // 🔥 중복 요청 체크
        boolean existsPendingRequest = memberEditProposalRepository.existsByMemberIdAndTargetFieldAndStatus(
                dto.getMemberId(),
                dto.getTargetField(),   // Enum -> String 변환
                MemberEditStatus.PENDING
        );

        if (existsPendingRequest) {
            throw new BusinessException(ResponseCode.DUPLICATE_EDIT_REQUEST);
        }

        // 🔥 엔티티 생성 및 저장
        MemberEditProposalEntity entity = MemberEditProposalEntity.builder()
                .memberId(dto.getMemberId())
                .targetField(dto.getTargetField())  // Enum -> String 변환
                .originalValue(dto.getOriginalValue())
                .requestedValue(dto.getRequestedValue())
                .fieldType(dto.getTargetField().getFieldType()) // Enum -> int 변환
                .reason(dto.getReason())
                .status(MemberEditStatus.PENDING)
                .requestedAt(LocalDateTime.now())
                .build();

        memberEditProposalRepository.save(entity);
    }

    @Override
    @Transactional
    public void approveEditProposal(int proposalId) {
        // 1️⃣ 권한 확인
        authFacade.checkHasRole(RoleCode.ROLE_HR_ACCESS);

        // 2️⃣ 요청 조회
        MemberEditProposalEntity proposal = memberEditProposalRepository.findById(proposalId)
                .orElseThrow(() -> new BusinessException(ResponseCode.EDIT_PROPOSAL_NOT_FOUND));

        // 3️⃣ 처리자(로그인 사용자) 가져오기
        Integer reviewerId = authFacade.getCurrentMemberId();

        // 4️⃣ 검증
        validateProposalCanBeProcessed(proposal, reviewerId);

        // 5️⃣ FK 검증 및 Member 값 변경
        applyFieldChange(proposal);

        // 6️⃣ 승인 처리
        proposal.approve(reviewerId);
    }

    /**
     * 공통 검증: 거절된 요청은 재처리 불가, 본인 요청은 본인이 처리 불가
     */
    private void validateProposalCanBeProcessed(MemberEditProposalEntity proposal, Integer approverId) {
        if (proposal.getStatus() == MemberEditStatus.REJECTED) {
            throw new BusinessException(ResponseCode.EDIT_PROPOSAL_ALREADY_REJECTED);
        }

        if (proposal.getMemberId().equals(approverId)) {
            throw new BusinessException(ResponseCode.EDIT_PROPOSAL_SELF_APPROVE_NOT_ALLOWED);
        }
    }

    /**
     * 실제 Member 값 변경
     */
    private void applyFieldChange(MemberEditProposalEntity proposal) {
        MemberEntity member = memberRepository.findById(proposal.getMemberId())
                .orElseThrow(() -> new BusinessException(ResponseCode.MEMBER_NOT_FOUND));

        MemberTargetField targetField = proposal.getTargetField();
        String requestedValue = proposal.getRequestedValue();

        switch (targetField) {
            case DEPARTMENT -> {
                Integer departmentId = Integer.valueOf(requestedValue);
                departmentRepository.findById(departmentId)
                        .orElseThrow(() -> new BusinessException(ResponseCode.DEPARTMENT_NOT_FOUND));
                member.updateDepartmentId(departmentId);
            }
            case POSITION -> {
                Integer positionId = Integer.valueOf(requestedValue);
                positionRepository.findById(positionId)
                        .orElseThrow(() -> new BusinessException(ResponseCode.POSITION_NOT_FOUND));
                member.updatePositionId(positionId);
            }
            case JOB -> {
                Integer jobId = Integer.valueOf(requestedValue);
                jobRepository.findById(jobId)
                        .orElseThrow(() -> new BusinessException(ResponseCode.JOB_NOT_FOUND));
                member.updateJobId(jobId);
            }
            case RANK -> {
                Integer rankId = Integer.valueOf(requestedValue);
                rankRepository.findById(rankId)
                        .orElseThrow(() -> new BusinessException(ResponseCode.RANK_NOT_FOUND));
                member.updateRankId(rankId);
            }
            case NAME -> member.updateName(requestedValue);
            case PHONE -> member.updatePhone(requestedValue);
            case EMAIL -> member.updateEmail(requestedValue);
            case ADDRESS -> member.updateAddress(requestedValue);
            case PICTURE_URL -> member.updatePictureUrl(requestedValue);
            default -> throw new BusinessException(ResponseCode.EDIT_PROPOSAL_INVALID_FIELD);
        }
    }


    @Override
    @Transactional
    public void reject(MemberEditRejectCommandDTO dto) {
        // 1️⃣ 권한 확인
        authFacade.checkHasRole(RoleCode.ROLE_HR_ACCESS);

        // 2️⃣ 요청 조회
        MemberEditProposalEntity proposal = memberEditProposalRepository.findById(dto.getEditProposalId())
                .orElseThrow(() -> new BusinessException(ResponseCode.EDIT_PROPOSAL_NOT_FOUND));

        // 3️⃣ 처리자(로그인 사용자) 가져오기
        Integer reviewerId = authFacade.getCurrentMemberId();

        // 4️⃣ 검증
        validateProposalCanBeProcessed(proposal, reviewerId);

        // 5️⃣ 거절 처리
        proposal.reject(reviewerId, dto.getRejectReason());
    }

}
