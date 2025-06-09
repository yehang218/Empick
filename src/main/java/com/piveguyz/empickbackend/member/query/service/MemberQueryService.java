package com.piveguyz.empickbackend.member.query.service;

import com.piveguyz.empickbackend.member.query.dto.MemberResponseDTO;

import java.util.List;

public interface MemberQueryService {
    MemberResponseDTO getMemberInfo(int memberId);

    String getProfileImageKey(int memberId);

    List<MemberResponseDTO>  getMembersByName(String name);

    List<MemberResponseDTO> getMembersByEmployeeNumber(int employeeNumbers);
}
