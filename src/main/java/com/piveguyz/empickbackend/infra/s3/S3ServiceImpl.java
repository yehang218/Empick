package com.piveguyz.empickbackend.infra.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3ServiceImpl implements S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    // 보안상 제한할 확장자 리스트 (블랙리스트)
    private static final List<String> BLACKLISTED_EXTENSIONS = List.of(
            "exe", "bat", "cmd", "sh", "js", "jar", "msi", "dll"
    );

    @Override
    public byte[] download(String key) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            ResponseBytes<GetObjectResponse> object = s3Client.getObjectAsBytes(request);
            log.info("S3 다운로드 성공 - key: {}, size: {}", key, object.asByteArray().length);

            return object.asByteArray();

        } catch (NoSuchKeyException e) {
            log.warn("존재하지 않는 파일 요청됨 - key: {}", key);
            throw e;

        } catch (IllegalArgumentException e) {
            log.error("버킷 설정 오류 - 환경 변수 누락 또는 잘못됨: {}", bucket);
            throw e;

        } catch (Exception e) {
            log.error("S3 다운로드 중 예기치 않은 오류 발생 - key: {}, message: {}", key, e.getMessage(), e);
            throw new RuntimeException("파일 다운로드 실패", e);
        }
    }

    // 사용자가 직접 파일명을 지정할 수 있는 업로드 메서드
    @Override
    public String uploadFile(String prefix, String fileName, MultipartFile file) {
        try {
            if (fileName == null || !fileName.contains(".")) {
                throw new IllegalArgumentException("파일 이름에 확장자가 없습니다.");
            }

            String ext = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

            // 블랙리스트 확장자 체크
            if (BLACKLISTED_EXTENSIONS.contains(ext)) {
                throw new IllegalArgumentException("보안상 업로드가 허용되지 않는 확장자입니다: " + ext);
            }

            // Key 생성
            String key = prefix.endsWith("/") ? prefix + fileName : prefix + "/" + fileName;

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
            log.info("S3 업로드 성공 - key: {}", key);

            return key;

        } catch (IOException e) {
            log.error("파일 업로드 실패 - prefix: {}", prefix, e);
            throw new RuntimeException("파일 업로드 실패");
        }
    }

    @Override
    public void delete(String key) {
        try {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            s3Client.deleteObject(request);
            log.info("S3 파일 삭제 완료 - key: {}", key);

        } catch (Exception e) {
            log.error("S3 파일 삭제 실패 - key: {}", key, e);
            throw new RuntimeException("파일 삭제 중 오류 발생");
        }
    }

    @Override
    public List<String> listFiles(String prefix) {
        try {
            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucket)
                    .prefix(prefix)
                    .build();

            ListObjectsV2Response response = s3Client.listObjectsV2(request);

            List<String> files = response.contents().stream()
                    .map(S3Object::key)
                    .toList();

            log.info("S3 파일 리스트 조회 성공 - prefix: {}, count: {}", prefix, files.size());
            return files;

        } catch (Exception e) {
            log.error("S3 파일 리스트 조회 실패 - prefix: {}", prefix, e);
            throw new RuntimeException("파일 리스트 조회 실패", e);
        }
    }
}
