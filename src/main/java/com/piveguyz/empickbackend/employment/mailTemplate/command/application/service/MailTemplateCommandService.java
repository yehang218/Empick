package com.piveguyz.empickbackend.employment.mailTemplate.command.application.service;


import com.piveguyz.empickbackend.employment.mailTemplate.command.application.dto.MailTemplateCommandDTO;

public interface MailTemplateCommandService {
    MailTemplateCommandDTO createTemplate(MailTemplateCommandDTO mailTemplateCommandDTO);

    MailTemplateCommandDTO updateTemplate(Integer id, MailTemplateCommandDTO mailTemplateCommandDTO);

    MailTemplateCommandDTO deleteTemplate(Integer id);
}
