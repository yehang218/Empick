package com.piveguyz.empickbackend.auth.facade;

import com.piveguyz.empickbackend.security.CustomMemberDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AuthFacadeTest {
    private AuthFacade authFacade;

    @BeforeEach
    void setUp() {
        authFacade = new AuthFacade();

        // Mock CustomMemberDetails
        CustomMemberDetails memberDetails = Mockito.mock(CustomMemberDetails.class);
        Mockito.when(memberDetails.getId()).thenReturn(42);
        Mockito.when(memberDetails.getUsername()).thenReturn("1001");

        // Mock Authorities
        List<GrantedAuthority> authorities = List.of(
                (GrantedAuthority) () -> "ROLE_USER",
                (GrantedAuthority) () -> "ROLE_ADMIN"
        );

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(memberDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testGetCurrentMemberId() {
        Integer memberId = authFacade.getCurrentMemberId();
        assertThat(memberId).isEqualTo(42);
    }

    @Test
    void testGetCurrentEmployeeNumber() {
        String employeeNumber = authFacade.getCurrentEmployeeNumber();
        assertThat(employeeNumber).isEqualTo("1001");
    }

    @Test
    void testGetCurrentUserRoles() {
        List<String> roles = authFacade.getCurrentUserRoles();
        assertThat(roles).containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
    }
}