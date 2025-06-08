package com.piveguyz.empickbackend.member.command.application.service;

import com.piveguyz.empickbackend.member.command.application.dto.MemberEditProposalCommandDTO;

public interface MemberEditProposalCommandService {
    /**
     * 사원 정보 수정 요청 생성
     *
     * @param dto 수정 요청 DTO
     */
    void createMemberEditRequest(MemberEditProposalCommandDTO dto);
}
