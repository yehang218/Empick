package com.piveguyz.empickbackend.employment.introduce.command.application.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntroduceTemplateCommandDTO {
    private Integer id;
    private String title;
    private Integer memberId;
}
