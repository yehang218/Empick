package com.piveguyz.empickbackend.infra.s3;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {

    byte[] download(String key);

    String uploadFile(String prefix, String fileName, MultipartFile file);

    void delete(String key);

    List<String> listFiles(String prefix);
}
