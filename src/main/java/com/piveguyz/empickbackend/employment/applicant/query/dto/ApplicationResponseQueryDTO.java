package com.piveguyz.empickbackend.employment.applicant.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApplicationResponseQueryDTO {

    @Schema(description = "응답 ID")
    private Long id;

    @Schema(description = "지원서 ID")
    private Long applicationId;

    @Schema(description = "지원서 항목 ID")
    private Long applicationItemId;

    @Schema(description = "응답 내용")
    private String content;
}