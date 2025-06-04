package com.piveguyz.empickbackend.employment.introduce.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IntroduceResultQueryDTO {
    private long id;
    private long memberId;
    private String content;
    private long ratingScore;
    private long introduceStandardId;
    private long status;
    private long updatedBy;
    private LocalDateTime updatedAt;
}
