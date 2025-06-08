package com.piveguyz.empickbackend.member.query.service;

import com.piveguyz.empickbackend.member.query.dto.MemberEditProposalQueryDTO;

import java.util.List;

public interface MemberEditProposalQueryService {
    List<MemberEditProposalQueryDTO> getProposals(Integer memberId);
}
