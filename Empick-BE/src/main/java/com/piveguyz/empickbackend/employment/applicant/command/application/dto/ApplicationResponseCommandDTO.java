package com.piveguyz.empickbackend.employment.applicant.command.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationResponseCommandDTO {
    private int applicationId;
    private int applicationItemId;
    private String content;
}
