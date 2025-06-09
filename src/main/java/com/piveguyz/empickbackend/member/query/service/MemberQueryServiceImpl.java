package com.piveguyz.empickbackend.member.query.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.member.command.domain.aggregate.Member;
import com.piveguyz.empickbackend.member.command.domain.repository.MemberRepository;
import com.piveguyz.empickbackend.member.query.dto.MemberResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService{

    private final MemberRepository memberRepository;

    @Override
    public MemberResponseDTO getMemberInfo(int memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        return new MemberResponseDTO(
                member.getId(),
                member.getName(),
                member.getEmployeeNumber(),
                member.getEmail()
        );
    }

    @Override
    public String getProfileImageKey(int memberId) {
        try {
        } catch (NumberFormatException e) {
            throw new BusinessException(ResponseCode.MEMBER_ID_INVALID);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ResponseCode.MEMBER_NOT_FOUND));

        String pictureUrl = member.getPictureUrl();
        if (pictureUrl == null || pictureUrl.isBlank()) {
            throw new BusinessException(ResponseCode.MEMBER_PROFILE_IMAGE_NOT_FOUND);
        }

        return pictureUrl;
    }
}
