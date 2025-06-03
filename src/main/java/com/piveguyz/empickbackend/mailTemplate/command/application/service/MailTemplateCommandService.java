package com.piveguyz.empickbackend.mailTemplate.command.application.service;


import com.piveguyz.empickbackend.mailTemplate.command.application.dto.MailTemplateCommandDTO;

public interface MailTemplateCommandService {
    MailTemplateCommandDTO createTemplate(MailTemplateCommandDTO mailTemplateCommandDTO);

    MailTemplateCommandDTO updateTemplate(MailTemplateCommandDTO mailTemplateCommandDTO);

    MailTemplateCommandDTO deleteTemplate(Integer id);
}
