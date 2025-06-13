package com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "introduce_rating_result")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntroduceRatingResultEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 자기소개서 평가 결과표 번호

    @Column(nullable = false)
    private String content; // 코멘트

    @Column(name = "rating_score", nullable = false)
    private Integer ratingScore; // 평가 점수

    @Column(nullable = false)
    private Integer status; // 제출 상태 (0: 확인전, 1: 합격, 2: 불합격)

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 수정 시각

    @Column(name = "updated_by")
    private Integer updatedBy; // 최종 수정자 ID

    @Column(name = "member_id", nullable = false)
    private Integer memberId; // 작성자 ID (인사팀)

    @Column(name = "introduce_standard_id", nullable = false)
    private Integer introduceStandardId; // 평가 기준표 ID

}

