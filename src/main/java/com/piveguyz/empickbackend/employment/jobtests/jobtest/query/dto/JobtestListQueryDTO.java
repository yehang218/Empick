package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class JobtestListQueryDTO {
    private int id;
    private String title;
    private int difficulty;
    private int createdId;
    private String createdName;
    private Integer updatedId;
    private String updatedName;
}
