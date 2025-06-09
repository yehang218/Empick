package com.piveguyz.empickbackend.employment.introduce.query.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IntroduceStandardQueryDTO {
    private Long id;
    private String content;
    private Long memberId;
    private Long introduceId;
    private Long introduceStandardItemId;
    private Long updatedBy;
    private LocalDateTime updatedAt;
}
