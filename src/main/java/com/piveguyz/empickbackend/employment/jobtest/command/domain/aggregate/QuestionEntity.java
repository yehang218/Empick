package com.piveguyz.empickbackend.employment.jobtest.command.domain.aggregate;

import com.piveguyz.empickbackend.employment.jobtest.command.domain.aggregate.enums.JobtestDifficulty;
import com.piveguyz.empickbackend.employment.jobtest.command.domain.aggregate.enums.JobtestType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Table(name = "question")
public class QuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "detail_content", nullable = true)
    private String detailContent;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type", nullable = false)
    private JobtestType type;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "difficulty", nullable = false)
    private JobtestDifficulty difficulty;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @Column(name = "created_member_id", nullable = false)
    private int createdMemberId;

    @Column(name = "updated_member_id", nullable = true)
    private Integer updatedMemberId;
}
