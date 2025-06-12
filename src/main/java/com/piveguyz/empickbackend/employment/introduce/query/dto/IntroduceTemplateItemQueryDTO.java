package com.piveguyz.empickbackend.employment.introduce.query.dto;

import lombok.*;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntroduceTemplateItemQueryDTO {
    private int Id;
    private String Title;
    private int MemberId;
    private Integer IntroduceTemplateId;
}
