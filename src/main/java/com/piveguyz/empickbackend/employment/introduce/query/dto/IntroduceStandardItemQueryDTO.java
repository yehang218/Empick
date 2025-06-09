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
    private Long id;
    private String content;
    private Long memberId;
    private Long updatedBy;
    private LocalDateTime updatedAt;
}
