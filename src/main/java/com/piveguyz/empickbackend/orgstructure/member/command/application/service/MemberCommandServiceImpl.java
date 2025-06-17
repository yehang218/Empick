package com.piveguyz.empickbackend.orgstructure.member.command.application.service;

import com.piveguyz.empickbackend.auth.facade.AuthFacade;
import com.piveguyz.empickbackend.common.constants.RoleCode;
import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.member.command.application.dto.MemberSignUpRequestDTO;
import com.piveguyz.empickbackend.orgstructure.member.command.application.dto.MemberSignUpResponseDTO;
import com.piveguyz.empickbackend.orgstructure.member.command.domain.aggregate.MemberEntity;
import com.piveguyz.empickbackend.orgstructure.member.command.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthFacade authFacade;

    @Override
    public MemberSignUpResponseDTO signUp(MemberSignUpRequestDTO request) {
        // 🔥 Step 1️⃣: 현재 로그인 사용자 ID 조회
        Integer createdMemberId = authFacade.getCurrentMemberId();

        // 🔥 Step 2️⃣: 권한 체크 (퍼사드 사용 + enum)
        authFacade.checkHasRole(RoleCode.ROLE_HR_ACCESS);

        // 🔥 Step 3️⃣: 입사처리자(현재 로그인 사용자) 조회
        MemberEntity createdMember = memberRepository.findById(createdMemberId)
                .orElseThrow(() -> new BusinessException(ResponseCode.MEMBER_CREATED_MEMBER_NOT_FOUND));

        // 🔥 Step 4️⃣: 이메일 중복 체크
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ResponseCode.MEMBER_EMAIL_DUPLICATED);
        }

        // 🔥 Step 5️⃣: EmployeeNumber 생성
        Integer employeeNumber = generateUniqueEmployeeNumber();

        // 🔥 Step 6️⃣: 신규 사원 생성
        MemberEntity member = MemberEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(employeeNumber.toString()))
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

        MemberEntity savedMember = memberRepository.save(member);

        return MemberSignUpResponseDTO.builder()
                .id(savedMember.getId())
                .email(savedMember.getEmail())
                .name(savedMember.getName())
                .employeeNumber(savedMember.getEmployeeNumber())
                .createdAt(LocalDate.from(savedMember.getCreatedAt()))
                .build();
    }

    @Override
    public void updateProfileImage(int memberId, String key) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ResponseCode.MEMBER_NOT_FOUND));

        member.updateProfileImageUrl(key);
    }

    @Override
    public void clearProfileImage(int memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ResponseCode.MEMBER_NOT_FOUND));

        member.clearProfileImageUrl();
    }

    @Override
    @Transactional
    public void resignMember(int memberId) {
        Integer currentMemberId = authFacade.getCurrentMemberId();

        authFacade.checkHasRole(RoleCode.ROLE_HR_ACCESS);

        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ResponseCode.MEMBER_NOT_FOUND));

        if (member.getResignAt() != null) {
            throw new BusinessException(ResponseCode.ALREDY_RESIGNED);
        }

        member.resign(LocalDateTime.now(), currentMemberId);
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
