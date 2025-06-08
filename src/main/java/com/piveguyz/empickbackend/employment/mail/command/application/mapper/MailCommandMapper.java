package com.piveguyz.empickbackend.employment.mail.command.application.mapper;

import com.piveguyz.empickbackend.employment.mail.command.application.dto.MailCommandDTO;
import com.piveguyz.empickbackend.employment.mail.command.domain.aggregate.MailEntity;
import org.springframework.stereotype.Component;

@Component
public class MailCommandMapper {
    public MailCommandDTO toDTO(MailEntity savedMailEntity) {
        MailCommandDTO mailCommandDTO = new MailCommandDTO();
        mailCommandDTO.setId(savedMailEntity.getId());
        mailCommandDTO.setApplicantId(savedMailEntity.getApplicantId());
        mailCommandDTO.setTitle(savedMailEntity.getTitle());
        mailCommandDTO.setContent(savedMailEntity.getContent());
        mailCommandDTO.setSenderId(savedMailEntity.getSenderId());
        mailCommandDTO.setSendedAt(savedMailEntity.getSendedAt());
        return mailCommandDTO;
    }

    public MailEntity toEntity(MailCommandDTO mailCommandDTO) {
        MailEntity mailEntity = new MailEntity();
        mailEntity.setId(mailCommandDTO.getId());
        mailEntity.setApplicantId(mailCommandDTO.getApplicantId());
        mailEntity.setTitle(mailCommandDTO.getTitle());
        mailEntity.setContent(mailCommandDTO.getContent());
        mailEntity.setSenderId(mailCommandDTO.getSenderId());
        mailEntity.setSendedAt(mailCommandDTO.getSendedAt());
        return mailEntity;
    }
}
