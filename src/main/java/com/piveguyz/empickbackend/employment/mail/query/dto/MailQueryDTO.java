package com.piveguyz.empickbackend.employment.mail.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailQueryDTO {
    private Integer id;
    private Integer applicantId;
    private String email;
    private String content;
    private Integer senderId;
    private LocalDateTime sendedAt;
}
