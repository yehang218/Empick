package com.piveguyz.empickbackend.employment.introduce.command.application.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntroduceTemplateCommandDTO {
    private Integer id;
    private String title;
    private Integer memberId;
    private List<Integer> itemIds;
}
