package com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "introduce_standard")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntroduceStandardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String content;

    @Column(name = "member_id", nullable = false)
    private Integer memberId;

}