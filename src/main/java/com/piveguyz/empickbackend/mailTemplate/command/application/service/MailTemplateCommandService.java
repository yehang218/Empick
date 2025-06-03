package com.piveguyz.empickbackend.mailTemplate.command.application.service;


import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.mailTemplate.command.application.dto.MailTemplateCommandDTO;

public interface MailTemplateCommandService {
    MailTemplateCommandDTO createTemplate(MailTemplateCommandDTO mailTemplateCommandDTO);

    ResponseCode updateTemplate(MailTemplateCommandDTO mailTemplateCommandDTO);

    ResponseCode deleteTemplate(Integer id);
}
