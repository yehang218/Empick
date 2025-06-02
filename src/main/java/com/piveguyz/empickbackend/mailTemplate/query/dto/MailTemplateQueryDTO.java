package com.piveguyz.empickbackend.mailTemplate.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailTemplateQueryDTO {
    private Integer id;
    private String title;
    private String content;
    private String isDeleted;
    private Integer memberId;
    private LocalDateTime updatedAt;
}
