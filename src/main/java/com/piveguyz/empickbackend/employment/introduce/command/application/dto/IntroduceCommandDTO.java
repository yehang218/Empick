package com.piveguyz.empickbackend.employment.introduce.command.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntroduceCommandDTO {
    private Integer id;
    private String content;
    private Integer introduceTemplateId;
    private Integer applicantId;
    private Integer applicationId;
}
