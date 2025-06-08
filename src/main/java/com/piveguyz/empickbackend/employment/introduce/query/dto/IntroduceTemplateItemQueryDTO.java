package com.piveguyz.empickbackend.employment.introduce.query.dto;

import lombok.*;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntroduceTemplateItemQueryDTO {
    private Long Id;
    private String Title;
    private Long MemberId;
}
