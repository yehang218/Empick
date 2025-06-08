package com.piveguyz.empickbackend.employment.mail.command.application.dto;

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
    private Integer applicantId;
    private String email;
    private String title;
    private String content;
    private Integer senderId;
    private LocalDateTime sendedAt;
}
