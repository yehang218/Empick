package com.piveguyz.empickbackend.infra.s3.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.infra.s3.dto.S3UploadResponseDTO;
import com.piveguyz.empickbackend.infra.s3.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
@Tag(name = "S3 파일 관리", description = "S3 파일 업로드, 다운로드, 삭제 API")
public class S3Controller {

    private final S3Service s3Service;

    @Operation(summary = "파일 다운로드", description = "S3에 저장된 파일을 다운로드합니다.")
    @GetMapping("/download")
    public ResponseEntity<?> getDownload(
            @Parameter(description = "다운로드할 S3 key", example = "docs/1/report.pdf")
            @RequestParam String key) {
        try {
            byte[] fileData = s3Service.download(key);
            String contentType = guessContentType(key);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(fileData);

        } catch (software.amazon.awssdk.services.s3.model.NoSuchKeyException e) {
            return ResponseEntity.status(404).body("파일이 존재하지 않습니다: " + key);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("요청 오류: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("서버 오류: " + e.getMessage());
        }
    }

    @Operation(summary = "파일 업로드", description = "prefix와 fileName을 직접 지정하여 파일을 업로드합니다.")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomApiResponse<S3UploadResponseDTO>> postUpload(
            @Parameter(description = "S3에 저장될 prefix 경로", example = "docs/1")
            @RequestParam String prefix,
            @Parameter(description = "S3에 저장될 파일명", example = "report.pdf")
            @RequestParam String fileName,
            @Parameter(description = "업로드할 파일", schema = @Schema(type = "string", format = "binary"))
            @RequestPart("file") MultipartFile file) {
        S3UploadResponseDTO response = s3Service.uploadFile(prefix, fileName, file);

        return ResponseEntity.status(HttpStatus.CREATED).body(CustomApiResponse.of(ResponseCode.CREATED, response));
    }

    @Operation(summary = "파일 삭제", description = "S3에 저장된 파일을 삭제합니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFile(
            @Parameter(description = "삭제할 S3 key", example = "docs/1/report.pdf")
            @RequestParam String key) {
        s3Service.delete(key);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "파일 목록 조회", description = "S3에 저장된 특정 prefix의 파일 리스트를 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<List<String>> listFiles(
            @Parameter(description = "조회할 S3 prefix 경로", example = "docs/1")
            @RequestParam String prefix) {
        List<String> files = s3Service.listFiles(prefix);
        return ResponseEntity.ok(files);
    }

    private String guessContentType(String key) {
        String lowerKey = key.toLowerCase();

        if (lowerKey.endsWith(".png")) return "image/png";
        if (lowerKey.endsWith(".jpg") || lowerKey.endsWith(".jpeg")) return "image/jpeg";
        if (lowerKey.endsWith(".webp")) return "image/webp";
        if (lowerKey.endsWith(".gif")) return "image/gif";
        if (lowerKey.endsWith(".svg")) return "image/svg+xml";
        if (lowerKey.endsWith(".pdf")) return "application/pdf";
        if (lowerKey.endsWith(".docx")) return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        if (lowerKey.endsWith(".xlsx")) return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        if (lowerKey.endsWith(".txt")) return "text/plain";
        if (lowerKey.endsWith(".zip")) return "application/zip";

        return "application/octet-stream";
    }
}
