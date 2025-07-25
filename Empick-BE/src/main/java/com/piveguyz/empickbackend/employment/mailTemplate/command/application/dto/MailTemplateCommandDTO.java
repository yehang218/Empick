package com.piveguyz.empickbackend.employment.mailTemplate.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailTemplateCommandDTO {
    private Integer id;
    private String title;
    private String content;
    private String isDeleted;
    private Integer memberId;
    private LocalDateTime updatedAt;
}
