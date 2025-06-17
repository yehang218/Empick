package com.piveguyz.empickbackend.employment.introduce.command.application.dto;

import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceRatingResultStatus;
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

    private IntroduceRatingResultStatus status; // ✅ enum으로 변경
    private String statusLabel; // ✅ 프론트용 라벨 포함

    private Integer updatedBy;
    private Integer memberId;
    private Integer introduceStandardId;
}
