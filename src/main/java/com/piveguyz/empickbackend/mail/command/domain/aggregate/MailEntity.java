package com.piveguyz.empickbackend.mail.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="mail")
public class MailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement
    @Column(name = "id")
    private Integer id;

    @Column(name = "applicant_id")
    private Integer applicantId;

    @Column(name = "email")
    private String email;

    @Column(name = "content")
    private String content;

    @Column(name = "sender_id")
    private Integer senderId;

    @Column(name = "sended_at")
    private LocalDateTime sendedAt;
}