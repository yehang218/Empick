package com.piveguyz.empickbackend.mailTemplate.query.service;

import com.piveguyz.empickbackend.mailTemplate.query.dto.MailTemplateQueryDTO;

import java.util.List;

public interface MailTemplateQueryService {
    List<MailTemplateQueryDTO> findAll();

    MailTemplateQueryDTO findByTitle(String title);

    List<MailTemplateQueryDTO> searchByTitle(String title);

    MailTemplateQueryDTO findById(Integer id);
}
