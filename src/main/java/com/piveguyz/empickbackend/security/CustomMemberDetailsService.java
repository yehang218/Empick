package com.piveguyz.empickbackend.security;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.member.command.domain.aggregate.MemberEntity;
import com.piveguyz.empickbackend.member.command.domain.repository.MemberRepository;
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
        int memberId;
        try {
            memberId = Integer.parseInt(username);
        } catch (NumberFormatException e) {
            throw new BusinessException(ResponseCode.BAD_REQUEST);
        }

        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ResponseCode.MEMBER_NOT_FOUND));

        if (member.getResignAt() != null) {
            throw new BusinessException(ResponseCode.MEMBER_RESIGNED);
        }

        return new CustomMemberDetails(member);
    }

}
