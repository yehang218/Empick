package com.piveguyz.empickbackend.employment.mail.command.application.service;


import com.piveguyz.empickbackend.employment.mail.command.application.dto.MailCommandDTO;

public interface MailCommandService {
    MailCommandDTO createMail(MailCommandDTO mailCommandDTO);
}
