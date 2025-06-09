package com.piveguyz.empickbackend.employment.mail.command.application.service;


import com.piveguyz.empickbackend.employment.mail.command.application.dto.MailCommandDTO;

import java.util.List;

public interface MailCommandService {
    List<String> createMail(MailCommandDTO mailCommandDTO);

    void sendSimpleMail(MailCommandDTO createdMailCommandDTO);

    void sendHTMLMail(MailCommandDTO createdMailCommandDTO);
}