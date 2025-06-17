package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto;

import com.piveguyz.empickbackend.employment.jobtests.common.enums.JobtestDifficulty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class JobtestListQueryDTO {
    private int id;
    private String title;
    private JobtestDifficulty difficulty;
    private int createdId;
    private String createdName;
    private String createMemberPictureUrl;
    private Integer updatedId;
    private String updatedName;
    private String updatedMemberPictureUrl;
}
