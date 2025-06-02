package com.piveguyz.empickbackend.mailTemplate.command.application.service;

import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.mailTemplate.command.application.dto.MailTemplateCommandDTO;
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
    private final MailTemplateQueryService mailTemplateQueryService;
    private final MailTemplateRepository mailTemplateRepository;
    private ModelMapper modelMapper;

    @Override
    public ResponseCode createTemplate(MailTemplateCommandDTO mailTemplateCommandDTO) {
        String title = mailTemplateCommandDTO.getTitle();
        String content = mailTemplateCommandDTO.getContent();
        if(title == null){
            return ResponseCode.EMPLOYMENT_MAIL_TEMPLATE_NO_TITLE;
        }
        if(content == null){
            return ResponseCode.EMPLOYMENT_MAIL_TEMPLATE_NO_CONTENT;
        }
        MailTemplateQueryDTO foundDTO = mailTemplateQueryService.findByTitle(title);
        if(foundDTO.getTitle().equals(title)){
            return ResponseCode.EMPLOYMENT_MAIL_TEMPLATE_DUPLICATE_TITLE;
        }
        MailTemplateEntity mailTemplateEntity = modelMapper.map(mailTemplateCommandDTO, MailTemplateEntity.class);
        // 저장 시도
        try {
            mailTemplateRepository.save(mailTemplateEntity);
            return ResponseCode.SUCCESS;
        } catch (Exception e) {
            return ResponseCode.BAD_REQUEST;
        }
    }

    @Override
    public ResponseCode updateTemplate(MailTemplateCommandDTO mailTemplateCommandDTO) {
        String title = mailTemplateCommandDTO.getTitle();
        String content = mailTemplateCommandDTO.getContent();
        if(title == null){
            return ResponseCode.EMPLOYMENT_MAIL_TEMPLATE_NO_TITLE;
        }
        if(content == null){
            return ResponseCode.EMPLOYMENT_MAIL_TEMPLATE_NO_CONTENT;
        }
        MailTemplateQueryDTO foundDTO = mailTemplateQueryService.findByTitle(title);
        if(foundDTO.getTitle().equals(title)){
            return ResponseCode.EMPLOYMENT_MAIL_TEMPLATE_DUPLICATE_TITLE;
        }
        MailTemplateEntity mailTemplateEntity = modelMapper.map(mailTemplateCommandDTO, MailTemplateEntity.class);
        // 수정 시도
        try {
            mailTemplateRepository.save(mailTemplateEntity);
            return ResponseCode.SUCCESS;
        } catch (Exception e) {
            return ResponseCode.BAD_REQUEST;
        }
    }

    @Override
    public ResponseCode deleteTemplate(Integer id) {
        MailTemplateQueryDTO foundDTO = mailTemplateQueryService.findById(id);
        if(foundDTO == null){
            return ResponseCode.BAD_REQUEST;
        }
        MailTemplateEntity mailTemplateEntity = modelMapper.map(foundDTO, MailTemplateEntity.class);
        mailTemplateEntity.setIsDeleted("Y");
        try {
            mailTemplateRepository.save(mailTemplateEntity);
            return ResponseCode.SUCCESS;
        } catch (Exception e) {
            return ResponseCode.BAD_REQUEST;
        }
    }
}
