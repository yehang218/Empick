package com.piveguyz.empickbackend.orgstructure.job.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobCommandDTO {

    @NotBlank(message = "직무 이름은 필수입니다.")
    private String name;

    @NotBlank(message = "직무 코드는 필수입니다.")
    private String code;

    @NotNull(message = "활성 여부는 필수입니다.")
    private Integer isActive;

    private String description;

    private Integer roleId;
}