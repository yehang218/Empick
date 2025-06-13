package com.piveguyz.empickbackend.orgstructure.department.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "부서 등록/수정 요청/응답 DTO")
public class DeptCommandDTO {

    @Schema(description = "부서 ID", example = "1")
    private Integer id;

    @Schema(description = "부서 이름", example = "인사팀")
    private String name;

    @Schema(description = "부서 코드", example = "HR001")
    private String code;

    @Schema(description = "부서 활성 여부", example = "1")
    private Integer isActive;

    @Schema(description = "부서 설명", example = "사내 인사 관련 업무를 담당합니다.")
    private String description;

    @Schema(description = "부서 권한 ID", example = "101")
    private Integer roleId;

    @Schema(description = "부서 생성일", example = "2024-01-01T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "부서 수정일", example = "2024-06-01T09:47:31")
    private LocalDateTime updatedAt;
}