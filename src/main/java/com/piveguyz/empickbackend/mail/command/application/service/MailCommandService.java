package com.piveguyz.empickbackend.mail.command.application.service;


import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.mail.command.application.dto.MailCommandDTO;

public interface MailCommandService {
    ResponseCode createMail(MailCommandDTO mailCommandDTO);
}
