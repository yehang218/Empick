package com.piveguyz.empickbackend.orgstructure.facade;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.infra.s3.dto.S3UploadResponseDTO;
import com.piveguyz.empickbackend.infra.s3.service.S3Service;
import com.piveguyz.empickbackend.orgstructure.member.command.application.dto.MemberProfileUploadRequestDTO;
import com.piveguyz.empickbackend.orgstructure.member.command.application.service.MemberCommandService;
import com.piveguyz.empickbackend.orgstructure.member.query.dto.MemberResponseDTO;
import com.piveguyz.empickbackend.orgstructure.member.query.service.MemberQueryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberProfileFacade {
    private final S3Service s3Service;
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    public String getProfileImageUrl(String key) {
        return s3Service.generatePublicUrl(key);
    }

    @Transactional
    public S3UploadResponseDTO uploadProfileImage(int memberId, MemberProfileUploadRequestDTO request) {
        S3UploadResponseDTO response = null;
        try {
            response = s3Service.uploadFile(
                    request.getPrefix(),
                    request.getFileName(),
                    request.getFile()
            );
            memberCommandService.updateProfileImage(memberId, response.getKey());
            return S3UploadResponseDTO.builder()
                    .key(response.getKey())
                    .contentType(response.getContentType())
                    .size(response.getSize())
                    .build();
        } catch (Exception e) {
            if (response != null) {
                s3Service.delete(response.getKey());
            }
            throw e;
        }
    }

    @Transactional
    public void deleteProfileImage(int memberId, String fileName) {
        s3Service.delete(fileName);
        memberCommandService.clearProfileImage(memberId);
    }

    public byte[] downloadProfileImage(int memberId) {
        // memberQueryService에서 이미 검증하므로 바로 사용
        String profileImageKey = getProfileImageKey(memberId);

        // S3에서 다운로드
        return s3Service.download(profileImageKey);
    }

    public String getProfileImageKey(int memberId) {
        // memberQueryService에서 이미 모든 검증을 수행하므로 바로 반환
        return memberQueryService.getProfileImageKey(memberId);
    }

    public MemberResponseDTO getMemberInfo(int memberId) {
        MemberResponseDTO response = memberQueryService.getMemberInfo(memberId);
        if (response == null) {
            throw new BusinessException(ResponseCode.MEMBER_NOT_FOUND);
        }

        return response;
    }
}