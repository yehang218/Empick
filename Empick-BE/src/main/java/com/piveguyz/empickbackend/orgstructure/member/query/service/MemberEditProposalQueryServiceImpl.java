package com.piveguyz.empickbackend.orgstructure.member.query.service;

import com.piveguyz.empickbackend.orgstructure.member.query.dto.MemberEditProposalQueryDTO;
import com.piveguyz.empickbackend.orgstructure.member.query.mapper.MemberEditProposalQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberEditProposalQueryServiceImpl implements MemberEditProposalQueryService {

    private final MemberEditProposalQueryMapper queryMapper;

    @Override
    public List<MemberEditProposalQueryDTO> getProposals(Integer memberId) {
        return queryMapper.findProposals(memberId);
    }
}
