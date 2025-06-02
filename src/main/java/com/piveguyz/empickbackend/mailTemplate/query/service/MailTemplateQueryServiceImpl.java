package com.piveguyz.empickbackend.mailTemplate.query.service;

import com.piveguyz.empickbackend.mailTemplate.query.dto.MailTemplateQueryDTO;
import com.piveguyz.empickbackend.mailTemplate.query.mapper.MailTemplateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailTemplateQueryServiceImpl implements MailTemplateQueryService {
    private final MailTemplateMapper mailTemplateMapper;

    @Override
    public List<MailTemplateQueryDTO> findAll() {
        List<MailTemplateQueryDTO> mailTemplateQueryDTOList = mailTemplateMapper.findAll();
        return mailTemplateQueryDTOList;
    }

    @Override
    public MailTemplateQueryDTO findByTitle(String title) {
        MailTemplateQueryDTO mailTemplateQueryDTO = mailTemplateMapper.findByTitle(title);
        return mailTemplateQueryDTO;
    }

    @Override
    public List<MailTemplateQueryDTO> searchByTitle(String title) {
        List<MailTemplateQueryDTO> mailTemplateQueryDTOList = mailTemplateMapper.searchByTitle(title);
        return List.of();
    }

    @Override
    public MailTemplateQueryDTO findById(Integer id) {
        MailTemplateQueryDTO mailTemplateQueryDTO = mailTemplateMapper.findById(id);
        return mailTemplateQueryDTO;
    }
}
