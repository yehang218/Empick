package com.piveguyz.empickbackend.mailTemplate.command.application.mapper;

import com.piveguyz.empickbackend.mailTemplate.command.application.dto.MailTemplateCommandDTO;
import com.piveguyz.empickbackend.mailTemplate.command.domain.aggregate.MailTemplateEntity;

public class MailTemplateCommandMapper {
    public static MailTemplateCommandDTO toDTO(MailTemplateEntity mailTemplateEntity) {
        MailTemplateCommandDTO mailTemplateCommandDTO = new MailTemplateCommandDTO();

        mailTemplateCommandDTO.setId(mailTemplateEntity.getId());
        mailTemplateCommandDTO.setTitle(mailTemplateEntity.getTitle());
        mailTemplateCommandDTO.setContent(mailTemplateEntity.getContent());
        mailTemplateCommandDTO.setIsDeleted(mailTemplateEntity.getIsDeleted());
        mailTemplateCommandDTO.setMemberId(mailTemplateEntity.getMemberId());
        mailTemplateCommandDTO.setUpdatedAt(mailTemplateEntity.getUpdatedAt());
        return mailTemplateCommandDTO;
    }

    public static MailTemplateEntity toEntity(MailTemplateCommandDTO mailTemplateCommandDTO) {
        MailTemplateEntity mailTemplateEntity = new MailTemplateEntity();
        mailTemplateEntity.setId(mailTemplateCommandDTO.getId());
        mailTemplateEntity.setTitle(mailTemplateCommandDTO.getTitle());
        mailTemplateEntity.setContent(mailTemplateCommandDTO.getContent());
        mailTemplateEntity.setIsDeleted(mailTemplateCommandDTO.getIsDeleted());
        mailTemplateEntity.setMemberId(mailTemplateCommandDTO.getMemberId());
        mailTemplateEntity.setUpdatedAt(mailTemplateCommandDTO.getUpdatedAt());
        return mailTemplateEntity;
    }
}
