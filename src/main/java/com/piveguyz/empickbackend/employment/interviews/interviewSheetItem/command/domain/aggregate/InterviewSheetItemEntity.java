package com.piveguyz.empickbackend.employment.interviews.interviewSheetItem.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="interview_sheet_item")
public class InterviewSheetItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement
    @Column(name = "id")
    private Integer id;

    @Column(name = "sheet_id")
    private Integer sheetId;

    @Column(name = "criteria_id")
    private Integer criteriaId;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
