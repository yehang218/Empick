package com.pive.empickbackend.security;

import com.pive.empickbackend.member.command.domain.aggregate.Member;
import com.pive.empickbackend.member.command.domain.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Autowired
    public CustomMemberDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        int empNum;
        try {
            empNum = Integer.parseInt(username);
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("유효하지 않은 사번입니다: " + username);
        }

        Member member = memberRepository.findByEmployeeNumber(empNum)
                .orElseThrow(() -> new UsernameNotFoundException("사원이 존재하지 않습니다."));

        return new CustomMemberDetails(member);
    }
}
