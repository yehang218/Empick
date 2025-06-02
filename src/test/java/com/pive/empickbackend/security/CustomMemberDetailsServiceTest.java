package com.pive.empickbackend.security;

import com.pive.empickbackend.member.command.domain.aggregate.Member;
import com.pive.empickbackend.member.command.domain.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomMemberDetailsServiceTest {

    @Autowired
    private CustomMemberDetailsService customMemberDetailsService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll(); // 테스트 시작 전에 DB 초기화
    }

    @Test
    void loadUserByUsername_올바른_사번() {
        // given: 테스트 데이터 insert
        Member member = new Member();
        member.setEmployeeNumber(1001);
        member.setPassword("encodedPassword");
        member.setName("테스트사원");
        member.setPhone("010-1234-5678");
        member.setPictureUrl("http://example.com/test.jpg");
        member.setEmail("test@example.com");
        member.setAddress("서울시 테스트구 테스트동");
        member.setHireAt(LocalDateTime.now());
        member.setCreatedAt(LocalDateTime.now());
        member.setStatus(1);
        memberRepository.save(member);

        // when
        String empNum = "1001";
        UserDetails userDetails = customMemberDetailsService.loadUserByUsername(empNum);

        // then
        assertNotNull(userDetails);
        assertTrue(userDetails instanceof CustomMemberDetails);
        assertEquals(empNum, userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_없는_사번() {
        String empNum = "999999";
        assertThrows(
                UsernameNotFoundException.class,
                () -> customMemberDetailsService.loadUserByUsername(empNum)
        );
    }
}
