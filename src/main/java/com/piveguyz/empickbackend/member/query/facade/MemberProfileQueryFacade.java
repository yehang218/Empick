package com.piveguyz.empickbackend.member.query.facade;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.infra.s3.service.S3Service;
import com.piveguyz.empickbackend.member.query.dto.MemberResponseDTO;
import com.piveguyz.empickbackend.member.query.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberProfileQueryFacade {
    private final S3Service s3Service;
    private final MemberQueryService memberQueryService;

    public byte[] downloadProfileImage(int memberId) {
        // 1. memberId로 pictureUrl 조회
        String profileImageKey = getProfileImageKey(memberId);
        if (profileImageKey == null || profileImageKey.isBlank()) {
            throw new BusinessException(ResponseCode.MEMBER_PROFILE_IMAGE_NOT_FOUND);
        }

        // 2. S3에서 다운로드
        return s3Service.download(profileImageKey);
    }

    public String getProfileImageKey(int memberId) {
        String profileImageKey = memberQueryService.getProfileImageKey(memberId);

        if (profileImageKey == null || profileImageKey.isBlank()) {
            throw new BusinessException(ResponseCode.MEMBER_PROFILE_IMAGE_NOT_FOUND);
        }
        return profileImageKey;
    }

    public MemberResponseDTO getMemberInfo(int memberId) {
        MemberResponseDTO response = memberQueryService.getMemberInfo(memberId);
        if (response == null) {
            throw new BusinessException(ResponseCode.MEMBER_NOT_FOUND);
        }

        return response;
    }
}
