package com.piveguyz.empickbackend.employment.introduce.command.application.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntroduceRatingResultCommandDTO {
    private Integer id;
    private String content;
    private Integer ratingScore;
    private Integer status;
    private LocalDateTime updatedAt;
    private Integer updatedBy;
    private Integer memberId;
    private Integer introduceStandardId;

}
