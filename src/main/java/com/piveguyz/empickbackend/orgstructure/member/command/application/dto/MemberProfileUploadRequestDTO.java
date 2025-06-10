package com.piveguyz.empickbackend.orgstructure.member.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MemberProfileUploadRequestDTO {
    private String prefix;
    private String fileName;
    private MultipartFile file;
}
