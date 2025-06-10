package com.piveguyz.empickbackend.employment.interviews.interviewSheetItem.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewSheetItemQueryDTO {
    private Integer id;
    private Integer sheetId;
    private Integer criteriaId;
    private Double weight;
    private Integer memberId;
    private LocalDateTime updatedAt;
}
