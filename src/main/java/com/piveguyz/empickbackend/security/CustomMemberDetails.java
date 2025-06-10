package com.piveguyz.empickbackend.security;

import com.piveguyz.empickbackend.orgstructure.member.command.domain.aggregate.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomMemberDetails implements UserDetails {

    private final MemberEntity member;

    public CustomMemberDetails(MemberEntity member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return String.valueOf(member.getEmployeeNumber());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // 추가로 Member 엔터티 반환을 위한 Getter
    public MemberEntity getMember() {
        return member;
    }

    // 🔥 Member ID 반환 메서드 추가
    public Integer getId() {
        return member.getId();
    }
}
