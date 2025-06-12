package com.piveguyz.empickbackend.orgstructure.member.command.application.service;

import com.piveguyz.empickbackend.orgstructure.member.command.application.dto.MemberEditApproveCommandDTO;
import com.piveguyz.empickbackend.orgstructure.member.command.application.dto.MemberEditProposalCommandDTO;
import com.piveguyz.empickbackend.orgstructure.member.command.application.dto.MemberEditRejectCommandDTO;
import com.piveguyz.empickbackend.orgstructure.member.command.application.dto.ProposalStatusUpdateDTO;

public interface MemberEditProposalCommandService {
    void createMemberEditRequest(MemberEditProposalCommandDTO dto);
    void approveEditProposal(int proposalId);
    void reject(MemberEditRejectCommandDTO dto);

    void updateEditProposalStatus(Integer memberId, Integer proposalId, ProposalStatusUpdateDTO statusUpdateDTO);
}
