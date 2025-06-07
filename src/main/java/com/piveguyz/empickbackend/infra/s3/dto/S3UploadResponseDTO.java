package com.piveguyz.empickbackend.infra.s3.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class S3UploadResponseDTO {
    private String key;           // S3 Key (prefix + 파일명)
    private String contentType;   // Content-Type
    private Long size;            // 파일 크기 (bytes)
}
