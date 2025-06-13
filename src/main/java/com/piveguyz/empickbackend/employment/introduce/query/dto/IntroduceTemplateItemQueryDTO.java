package com.piveguyz.empickbackend.employment.introduce.query.dto;

import lombok.*;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntroduceTemplateItemQueryDTO {
    private int id;
    private String title;
    private int memberId;
    private Integer introduceTemplateId;
}
