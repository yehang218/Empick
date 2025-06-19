package com.piveguyz.empickbackend.employment.introduce.command.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntroduceTemplateItemResponseCommandDTO {
    private Integer introduceId;
    private Integer introduceTemplateItemId;
    private String content;
}
