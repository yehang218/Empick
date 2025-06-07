package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobtestQuestionListQueryDTO {
    private int id;
    private String title;
    private int difficulty;
    private String creatorName;
    private String updatedName;
}
