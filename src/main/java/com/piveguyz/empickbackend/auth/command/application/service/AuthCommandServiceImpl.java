package com.piveguyz.empickbackend.auth.command.application.service;

import com.piveguyz.empickbackend.auth.command.application.dto.LoginRequestDTO;
import com.piveguyz.empickbackend.auth.command.application.dto.LoginResponseDTO;
import com.piveguyz.empickbackend.auth.command.jwt.JwtProvider;
import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.member.command.domain.aggregate.Member;
import com.piveguyz.empickbackend.member.command.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthCommandServiceImpl implements AuthCommandService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public LoginResponseDTO login(LoginRequestDTO requestDTO) {
        Member member = memberRepository.findByEmployeeNumber(
                Integer.parseInt(
                        requestDTO.getEmployeeNumber()
                )
        ).orElseThrow(() -> new BusinessException(ResponseCode.BAD_REQUEST));

        if (!passwordEncoder.matches(requestDTO.getPassword(), member.getPassword())) {
            throw new BusinessException(ResponseCode.BAD_REQUEST); // 로그인 실패의 정확한 원인을 밝히지 않기 위함
        }

        String accessToken = jwtProvider.createAccessToken(member.getId(), member.getEmployeeNumber());
        String refreshToken = jwtProvider.createRefreshToken(member.getId(), member.getEmployeeNumber());

        return LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
