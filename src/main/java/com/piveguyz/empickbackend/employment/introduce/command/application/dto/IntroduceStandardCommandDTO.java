package com.piveguyz.empickbackend.employment.introduce.command.application.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntroduceStandardCommandDTO {
    private Integer id;
    private String content;
    private Integer memberId;
}
