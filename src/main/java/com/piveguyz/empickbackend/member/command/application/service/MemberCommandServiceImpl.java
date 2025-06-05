package com.piveguyz.empickbackend.member.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.member.command.application.dto.MemberSignUpRequestDTO;
import com.piveguyz.empickbackend.member.command.application.dto.MemberSignUpResponseDTO;
import com.piveguyz.empickbackend.member.command.domain.aggregate.Member;
import com.piveguyz.empickbackend.member.command.domain.repository.MemberRepository;
import com.piveguyz.empickbackend.security.CustomMemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberSignUpResponseDTO signUp(MemberSignUpRequestDTO request) {
        // 🔥 Step 1️⃣: 현재 로그인 사용자 정보 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomMemberDetails)) {
            throw new BusinessException(ResponseCode.UNAUTHORIZED);
        }

        CustomMemberDetails currentUser = (CustomMemberDetails) authentication.getPrincipal();
        Integer createdMemberId = currentUser.getId();

        // 🔥 Step 2️⃣: 현재 로그인 사용자의 권한 확인
        if (authentication.getAuthorities().stream()
                .noneMatch(auth -> auth.getAuthority().equals("ROLE_HR_ACCESS"))) {
            throw new BusinessException(ResponseCode.MEMBER_CREATED_MEMBER_NO_PERMISSION);
        }

        // 🔥 Step 3️⃣: 입사처리자(현재 로그인 사용자) 조회
        Member createdMember = memberRepository.findById(createdMemberId)
                .orElseThrow(() -> new BusinessException(ResponseCode.MEMBER_CREATED_MEMBER_NOT_FOUND));

        // 🔥 Step 4️⃣: 이메일 중복 체크
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ResponseCode.MEMBER_EMAIL_DUPLICATED);
        }

        // 🔥 Step 5️⃣: EmployeeNumber 생성
        int employeeNumber = generateUniqueEmployeeNumber();

        // 🔥 Step 6️⃣: 신규 사원 생성
        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .birth(request.getBirth())
                .pictureUrl(request.getPictureUrl())
                .employeeNumber(employeeNumber)
                .hireAt(request.getHireAt())
                .status(1) // 기본 활성 상태
                .createdAt(LocalDateTime.now())
                .createdMemberId(createdMemberId)
                .build();

        Member savedMember = memberRepository.save(member);

        return MemberSignUpResponseDTO.builder()
                .id(savedMember.getId())
                .email(savedMember.getEmail())
                .name(savedMember.getName())
                .employeeNumber(savedMember.getEmployeeNumber())
                .createdAt(LocalDate.from(savedMember.getCreatedAt()))
                .build();
    }

    /**
     * 랜덤한 6자리 EmployeeNumber를 생성하고 중복 방지.
     */
    private Integer generateUniqueEmployeeNumber() {
        int maxAttempts = 10;
        for (int i = 0; i < maxAttempts; i++) {
            int randomNumber = (int) (Math.random() * 900000) + 100000; // 100000 ~ 999999
            if (!memberRepository.existsByEmployeeNumber(randomNumber)) {
                return randomNumber;
            }
        }
        throw new BusinessException(ResponseCode.MEMBER_EMPLOYEE_NUMBER_DUPLICATE);
    }
}
