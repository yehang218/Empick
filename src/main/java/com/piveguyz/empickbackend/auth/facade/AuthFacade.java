package com.piveguyz.empickbackend.auth.facade;

import com.piveguyz.empickbackend.common.constants.RoleCode;
import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.security.CustomMemberDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthFacade {

    public Integer getCurrentMemberId() {
        Authentication auth = getAuthentication();
        CustomMemberDetails principal = (CustomMemberDetails) auth.getPrincipal();
        return principal.getId();
    }

    public String getCurrentEmployeeNumber() {
        Authentication auth = getAuthentication();
        return auth.getName();
    }

    public List<String> getCurrentUserRoles() {
        Authentication auth = getAuthentication();
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    /**
     * 특정 권한이 있는지 검증 후 없으면 BusinessException 발생
     */
    public void checkHasRole(RoleCode requiredRole) {
        List<String> roles = getCurrentUserRoles();
        if (roles.stream().noneMatch(role -> role.equals(requiredRole.name()))) {
            throw new BusinessException(ResponseCode.MEMBER_CREATED_MEMBER_NO_PERMISSION);
        }
    }

    /**
     * 주어진 권한 중 하나라도 있으면 통과, 없으면 BusinessException 발생
     */
    public void checkHasAnyRole(RoleCode... requiredRoles) {
        List<String> roles = getCurrentUserRoles();
        boolean hasRole = roles.stream()
                .anyMatch(role -> List.of(requiredRoles).stream()
                        .anyMatch(requiredRole -> role.equals(requiredRole.name())));
        if (!hasRole) {
            throw new BusinessException(ResponseCode.MEMBER_CREATED_MEMBER_NO_PERMISSION);
        }
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
