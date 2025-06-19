package com.piveguyz.empickbackend.employment.introduce.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntroduceTemplateItemResponseQueryDTO {

    private Integer id;
    private Integer introduceId;
    private Integer introduceTemplateItemId;
    private String content;
}
