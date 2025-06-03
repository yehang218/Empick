package com.piveguyz.empickbackend.member.query.service;

import com.piveguyz.empickbackend.member.query.dto.MemberResponseDTO;

public interface MemberQueryService {
    MemberResponseDTO getMemberInfo(int memberId);
}
