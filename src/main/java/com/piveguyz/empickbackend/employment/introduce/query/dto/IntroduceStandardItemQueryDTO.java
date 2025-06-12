package com.piveguyz.empickbackend.employment.introduce.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IntroduceStandardItemQueryDTO {
    private Integer id;
    private String content;
    private Integer memberId;
    private Integer updatedBy;
    private LocalDateTime updatedAt;
    private Integer introduceStandardId;
}
