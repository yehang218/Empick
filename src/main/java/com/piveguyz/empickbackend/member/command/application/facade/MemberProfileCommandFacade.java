package com.piveguyz.empickbackend.member.command.application.facade;

import com.piveguyz.empickbackend.infra.s3.dto.S3UploadResponseDTO;
import com.piveguyz.empickbackend.infra.s3.service.S3Service;
import com.piveguyz.empickbackend.member.command.application.dto.MemberProfileUploadRequestDTO;
import com.piveguyz.empickbackend.member.command.application.service.MemberCommandService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberProfileCommandFacade {
    private final S3Service s3Service;
    private final MemberCommandService memberCommandService;

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
}
