package com.piveguyz.empickbackend.employment.introduce.query.dto;

import lombok.Getter;
import lombok.Setter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntroduceTemplateQueryDTO {
    private int id;
    private String title;
    private int memberId;
    private List<IntroduceTemplateItemQueryDTO> items;
}
