package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto;

import com.piveguyz.empickbackend.employment.jobtests.common.enums.JobtestDifficulty;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateJobtestCommandDTO {
    private String title;
    private JobtestDifficulty difficulty;
    private Integer testTime;
    private Integer jobTestTypeId;
    private int updatedMemberId;
}
