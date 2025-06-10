package com.piveguyz.empickbackend.member.command.application.service;

import com.piveguyz.empickbackend.member.command.application.dto.MemberSignUpRequestDTO;
import com.piveguyz.empickbackend.member.command.application.dto.MemberSignUpResponseDTO;

public interface MemberCommandService {
    MemberSignUpResponseDTO signUp(MemberSignUpRequestDTO request);

    void updateProfileImage(int memberId, String key);

    void clearProfileImage(int memberId);

    void resignMember(int memberId);
}
