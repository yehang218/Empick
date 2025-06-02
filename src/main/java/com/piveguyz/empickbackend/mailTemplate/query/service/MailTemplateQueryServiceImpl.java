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
        List<MailTemplateQueryDTO> mailTemplateQueryDTOList = new ArrayList<>();
        mailTemplateQueryDTOList = mailTemplateMapper.findAll();
        return mailTemplateQueryDTOList;
    }

    @Override
    public List<MailTemplateQueryDTO> findByTitle(String title) {
        List<MailTemplateQueryDTO> mailTemplateQueryDTOList = new ArrayList<>();
        mailTemplateQueryDTOList = mailTemplateMapper.findByTitle(title);
        return mailTemplateQueryDTOList;
    }
}
