package com.piveguyz.empickbackend.security;

import com.piveguyz.empickbackend.member.command.domain.aggregate.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomMemberDetails implements UserDetails {

    private final Member member;

    public CustomMemberDetails(Member member) {
        this.member = member;
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        // 예: Member에 권한(Role)이 단일 문자열이라면 이렇게 처리
//        return Collections.singleton(new SimpleGrantedAuthority(member.getRole()));
//    }

    // TODO: role 테이블을 설계 완료하면 위의 주석 내용으로 구체화
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
        return true; // 계정 만료 여부. 여기선 항상 true로 설정
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠김 여부. 여기선 항상 true로 설정
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호 만료 여부. 여기선 항상 true로 설정
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부. 여기선 항상 true로 설정
    }

    // 추가로 Member 엔터티 반환을 위한 Getter
    public Member getMember() {
        return member;
    }
}
