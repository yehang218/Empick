package com.piveguyz.empickbackend.employment.jobtests.evaluation.query.dto;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class EvaluationCriteriaQueryDTO {
    // 내용, 상세 내용, 가중치
    private int id;
    private String content;
    private String detailContent;
    private double scoreWeight;
    private int jobtestId;
}
