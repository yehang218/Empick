package com.piveguyz.empickbackend.infra.s3.service;

import com.piveguyz.empickbackend.infra.s3.dto.S3UploadResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {

    byte[] download(String key);

    S3UploadResponseDTO uploadFile(String prefix, String fileName, MultipartFile file);

    void delete(String key);

    List<String> listFiles(String prefix);

    String generatePublicUrl(String key);
}
