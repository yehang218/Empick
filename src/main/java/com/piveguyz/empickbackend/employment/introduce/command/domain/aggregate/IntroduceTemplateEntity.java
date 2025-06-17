package com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "introduce_template")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntroduceTemplateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(name = "member_id", nullable = false)
    private Integer memberId;
}

