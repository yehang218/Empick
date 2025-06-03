package com.piveguyz.empickbackend.member.query.service;

import com.piveguyz.empickbackend.member.command.domain.aggregate.Member;
import com.piveguyz.empickbackend.member.command.domain.repository.MemberRepository;
import com.piveguyz.empickbackend.member.query.dto.MemberResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService{

    private final MemberRepository memberRepository;

    @Override
    public MemberResponseDTO getMemberInfo(int memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        return new MemberResponseDTO(
                member.getId(),
                member.getName(),
                member.getEmployeeNumber(),
                member.getEmail()
        );
    }
}
