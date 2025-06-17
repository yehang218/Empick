package com.piveguyz.empickbackend.employment.jobtests.grading.query.dto;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class GradingCriteriaQueryDTO {
    private int id;
    private String content;
    private String detailContent;
    private double scoreWeight;
}
