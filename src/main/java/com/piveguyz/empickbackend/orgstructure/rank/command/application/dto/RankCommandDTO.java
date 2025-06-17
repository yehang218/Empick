package com.piveguyz.empickbackend.orgstructure.rank.command.application.dto;

import com.piveguyz.empickbackend.common.enums.IsActive;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 직급 Command DTO
 * 직급 생성, 수정, 삭제 등의 명령 처리를 위한 데이터 전송 객체
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "직급 Command DTO")
public class RankCommandDTO {

    @Schema(description = "직급 ID (수정/삭제 시 사용)", example = "1")
    private Integer id;

    @Schema(description = "직급명", example = "사원", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "직급명은 필수입니다.")
    private String name;

    @Schema(
            description = "직급 코드 (영문 대소문자, 숫자, 언더스코어 조합)",
            example = "RANK_TEST",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "직급 코드는 필수입니다.")
    @Pattern(
            regexp = "^[A-Za-z0-9_]{2,20}$",
            message = "직급 코드는 2-20자리 영문자, 숫자, 언더스코어(_)만 사용 가능합니다."
    )
    private String code;

    @Schema(description = "활성 여부", example = "ACTIVE", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "활성 여부는 필수입니다.")
    private IsActive isActive;

    @Schema(description = "급여 밴드", example = "5000")
    @Positive(message = "급여 밴드는 양수여야 합니다.")
    private Integer salaryBand;

    @Schema(description = "권한 ID", example = "5")
    @Positive(message = "권한 ID는 양수여야 합니다.")
    private Integer roleId;

    @Schema(description = "생성일시", example = "2024-01-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2024-01-01T10:00:00")
    private LocalDateTime updatedAt;
}