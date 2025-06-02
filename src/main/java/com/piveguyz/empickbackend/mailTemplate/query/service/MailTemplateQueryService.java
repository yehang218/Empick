package com.piveguyz.empickbackend.mailTemplate.query.service;

import com.piveguyz.empickbackend.mailTemplate.query.dto.MailTemplateQueryDTO;

import java.util.List;

public interface MailTemplateQueryService {
    List<MailTemplateQueryDTO> findAll();

    List<MailTemplateQueryDTO> findByTitle(String title);
}
