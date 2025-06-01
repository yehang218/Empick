package com.piveguyz.empickbackend.mail.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailCommandDTO {
    private Integer id;
    private Integer sheetId;
    private Integer criteriaId;
    private Integer weight;
    private Integer memberId;
    private LocalDateTime updatedAt;
}
