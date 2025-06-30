package com.piveguyz.empickbackend.employment.applicant.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Table(name = "application_response")
public class ApplicationResponseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  // 지원서 항목별 내용 번호 (PK)

    @Column(name = "application_id", nullable = false)
    private Integer applicationId;  // 지원서 번호 (FK)

    @Column(name = "application_item_id", nullable = false)
    private Integer applicationItemId;  // 공고별 지원서 항목 번호 (FK)

    @Column(name = "content", columnDefinition = "LONGTEXT", nullable = false)
    private String content;  // 응답 내용
}
