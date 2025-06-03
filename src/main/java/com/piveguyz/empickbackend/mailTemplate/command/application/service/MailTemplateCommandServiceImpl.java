package com.piveguyz.empickbackend.mailTemplate.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.mailTemplate.command.application.dto.MailTemplateCommandDTO;
import com.piveguyz.empickbackend.mailTemplate.command.application.mapper.MailTemplateCommandMapper;
import com.piveguyz.empickbackend.mailTemplate.command.domain.aggregate.MailTemplateEntity;
import com.piveguyz.empickbackend.mailTemplate.command.domain.repository.MailTemplateRepository;
import com.piveguyz.empickbackend.mailTemplate.query.dto.MailTemplateQueryDTO;
import com.piveguyz.empickbackend.mailTemplate.query.service.MailTemplateQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailTemplateCommandServiceImpl implements MailTemplateCommandService {
    private final MailTemplateRepository mailTemplateRepository;
    private final MailTemplateCommandMapper mailTemplateCommandMapper;

    @Override
    public MailTemplateCommandDTO createTemplate(MailTemplateCommandDTO mailTemplateCommandDTO) {
        String title = mailTemplateCommandDTO.getTitle();
        String content = mailTemplateCommandDTO.getContent();
        if(title == null){
            throw new BusinessException(ResponseCode.EMPLOYMENT_MAIL_TEMPLATE_NO_TITLE);
        }
        if(content == null){
            throw new BusinessException(ResponseCode.EMPLOYMENT_MAIL_TEMPLATE_NO_CONTENT);
        }
        if(mailTemplateRepository.existsByTitle(title)){
            throw new BusinessException(ResponseCode.EMPLOYMENT_MAIL_TEMPLATE_DUPLICATE_TITLE);
        }

        MailTemplateEntity mailTemplateEntity = mailTemplateCommandMapper.toEntity(mailTemplateCommandDTO);
        MailTemplateEntity createdMailTemplateEntity = mailTemplateRepository.save(mailTemplateEntity);
        MailTemplateCommandDTO createdMailTemplateCommandDTO = mailTemplateCommandMapper.toDTO(createdMailTemplateEntity);
        return createdMailTemplateCommandDTO;
    }

    @Override
    public MailTemplateCommandDTO updateTemplate(MailTemplateCommandDTO mailTemplateCommandDTO) {
        String title = mailTemplateCommandDTO.getTitle();
        String content = mailTemplateCommandDTO.getContent();
        if(title == null){
            throw new BusinessException(ResponseCode.EMPLOYMENT_MAIL_TEMPLATE_NO_TITLE);
        }
        if(content == null){
            throw new BusinessException(ResponseCode.EMPLOYMENT_MAIL_TEMPLATE_NO_CONTENT);
        }
        if (mailTemplateRepository.existsByTitleAndIdNot(title, mailTemplateCommandDTO.getId())) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_MAIL_TEMPLATE_DUPLICATE_TITLE);
        }

        MailTemplateEntity mailTemplateEntity = mailTemplateCommandMapper.toEntity(mailTemplateCommandDTO);
        MailTemplateEntity updatedMailTemplateEntity = mailTemplateRepository.save(mailTemplateEntity);
        MailTemplateCommandDTO updatedMailTemplateCommandDTO = mailTemplateCommandMapper.toDTO(updatedMailTemplateEntity);
        return updatedMailTemplateCommandDTO;
    }

    @Override
    public MailTemplateCommandDTO deleteTemplate(Integer id) {
        if(!mailTemplateRepository.existsById(id)){
            throw new BusinessException(ResponseCode.EMPLOYMENT_MAIL_TEMPLATE_NOT_FOUND);
        }
        MailTemplateEntity mailTemplateEntity = mailTemplateRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_MAIL_TEMPLATE_NOT_FOUND));

        mailTemplateEntity.setIsDeleted("Y");
        mailTemplateEntity = mailTemplateRepository.save(mailTemplateEntity);
        MailTemplateCommandDTO deletedMailTemplateCommandDTO = mailTemplateCommandMapper.toDTO(mailTemplateEntity);
        return deletedMailTemplateCommandDTO;
    }
}
