package com.piveguyz.empickbackend.orgstructure.member.query.mapper;

import com.piveguyz.empickbackend.orgstructure.member.query.dto.MemberEditProposalQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberEditProposalQueryMapper {
    MemberEditProposalQueryDTO findById(@Param("id") Integer id);
    List<MemberEditProposalQueryDTO> findProposals(@Param("memberId") Integer memberId);
}
