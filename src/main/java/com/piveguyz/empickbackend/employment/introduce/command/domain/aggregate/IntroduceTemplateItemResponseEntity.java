package com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "introduce_template_item_response")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntroduceTemplateItemResponseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "introduce_id", nullable = false)
    private Integer introduceId;

    @Column(name = "introduce_template_item_id", nullable = false)
    private Integer introduceTemplateItemId;

    @Column(name = "content", nullable = false, columnDefinition = "LONGTEXT")
    private String content;
}
