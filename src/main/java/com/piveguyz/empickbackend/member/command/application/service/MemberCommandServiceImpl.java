package com.piveguyz.empickbackend.member.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.member.command.application.dto.MemberSignUpRequestDTO;
import com.piveguyz.empickbackend.member.command.application.dto.MemberSignUpResponseDTO;
import com.piveguyz.empickbackend.member.command.domain.aggregate.Member;
import com.piveguyz.empickbackend.member.command.domain.repository.MemberRepository;
import com.piveguyz.empickbackend.security.CustomMemberDetails;
import com.piveguyz.empickbackend.security.CustomMemberDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomMemberDetailsService customMemberDetailsService;

    @Override
    public MemberSignUpResponseDTO signUp(MemberSignUpRequestDTO request) {

        // createdMemberId(입사처리자) 필수 확인
        if (request.getCreatedMemberId() == null) {
            throw new BusinessException(ResponseCode.MEMBER_CREATED_MEMBER_ID_REQUIRED);
        }

        // 입사처리자 조회
        Member createdMember = memberRepository.findById(request.getCreatedMemberId())
                .orElseThrow(() -> new BusinessException(ResponseCode.MEMBER_CREATED_MEMBER_NOT_FOUND));

        // 권한 확인
        CustomMemberDetails createdCustomMemberDetails = customMemberDetailsService.loadUserByUsername(createdMember.getEmail());

//        if (createdMemeberDetails.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_HR")))
//        {
////           // TODO: 입사처리자 로그인 -> 권한 확인 (부서 테이블에서 조인) -> 사원 등록 (개별, 다수) -> 신규 사원 등록 처리
//        }
        // 이메일 중복체크
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ResponseCode.MEMBER_EMAIL_DUPLICATED);
        }

        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phone(request.getPhone())
                .createdAt(LocalDateTime.now())
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
}
