package com.piveguyz.empickbackend.member.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.member.command.application.dto.MemberEditProposalCommandDTO;
import com.piveguyz.empickbackend.member.command.domain.aggregate.MemberEditProposalEntity;
import com.piveguyz.empickbackend.member.command.domain.enums.MemberEditStatus;
import com.piveguyz.empickbackend.member.command.domain.repository.MemberEditProposalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberEditProposalCommandServiceImpl implements MemberEditProposalCommandService {

    private final MemberEditProposalRepository memberEditRepository;

    @Override
    @Transactional
    public void createMemberEditRequest(MemberEditProposalCommandDTO dto) {
        // 🔥 중복 요청 체크
        boolean existsPendingRequest = memberEditRepository.existsByMemberIdAndTargetFieldAndStatus(
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

        memberEditRepository.save(entity);
    }
}
