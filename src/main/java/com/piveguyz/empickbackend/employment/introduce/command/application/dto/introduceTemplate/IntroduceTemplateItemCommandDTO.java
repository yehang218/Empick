package com.piveguyz.empickbackend.employment.introduce.command.application.dto.introduceTemplate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IntroduceTemplateItemCommandDTO {
    private Integer id;
    private String title;
    private Integer memberId;
    private Integer introduceTemplateId;
}
