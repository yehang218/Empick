package com.piveguyz.empickbackend.employment.introduce.query.dto;

import lombok.Getter;
import lombok.Setter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntroduceTemplateQueryDTO {
    private Long id;
    private String title;
    private Long memberId;
    private Long introduceTemplateItemId;

}
