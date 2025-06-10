package com.piveguyz.empickbackend.orgstructure.member.query.service;

import com.piveguyz.empickbackend.orgstructure.member.query.dto.MemberEditProposalQueryDTO;

import java.util.List;

public interface MemberEditProposalQueryService {
    List<MemberEditProposalQueryDTO> getProposals(Integer memberId);
}
