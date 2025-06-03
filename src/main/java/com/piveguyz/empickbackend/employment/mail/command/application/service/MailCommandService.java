package com.piveguyz.empickbackend.employment.mail.command.application.service;


import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.mail.command.application.dto.MailCommandDTO;

public interface MailCommandService {
    ResponseCode createMail(MailCommandDTO mailCommandDTO);
}
