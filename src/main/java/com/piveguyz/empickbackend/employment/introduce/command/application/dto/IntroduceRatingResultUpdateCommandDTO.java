package com.piveguyz.empickbackend.employment.introduce.command.application.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntroduceRatingResultUpdateCommandDTO {
    private Integer id;
    private String content;
    private Integer ratingScore;
    private Integer updatedBy;
}
