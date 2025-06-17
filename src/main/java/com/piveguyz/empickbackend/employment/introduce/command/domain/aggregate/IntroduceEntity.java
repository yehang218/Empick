package com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "introduce")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntroduceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String content;

    @Column(name = "introduce_template_id", nullable = false)
    private Integer introduceTemplateId;

    @Column(name = "applicant_id", nullable = false)
    private Integer applicantId;
}
