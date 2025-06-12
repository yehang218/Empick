package com.piveguyz.empickbackend.employment.introduce.command.application.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IntroduceStandardItemCommandDTO {
    private Integer id;
    private String content;
    private Integer memberId;
    private Integer introduceStandardId;
}
