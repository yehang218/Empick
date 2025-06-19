package com.piveguyz.empickbackend.employment.introduce.command.application.dto;

import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceRatingResultStatus;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntroduceRatingResultCreateCommandDTO {
    private String content;
    private Integer ratingScore;
    private Integer memberId;
    private Integer introduceStandardId;
}
