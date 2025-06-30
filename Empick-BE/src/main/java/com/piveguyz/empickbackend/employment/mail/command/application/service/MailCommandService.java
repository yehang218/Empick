package com.piveguyz.empickbackend.employment.mail.command.application.service;


import com.piveguyz.empickbackend.employment.mail.command.application.dto.MailCommandDTO;

import java.util.List;

public interface MailCommandService {
    MailCommandDTO createMail(MailCommandDTO mailCommandDTO);

    MailCommandDTO sendSimpleMail(MailCommandDTO createdMailCommandDTO);

    MailCommandDTO sendHTMLMail(MailCommandDTO createdMailCommandDTO);

    MailCommandDTO sendJobtestMail(Integer applicationId, Integer senderId);

    MailCommandDTO sendInterviewMail(Integer applicationId, Integer senderId);
}