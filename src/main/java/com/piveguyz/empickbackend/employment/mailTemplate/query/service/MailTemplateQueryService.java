package com.piveguyz.empickbackend.employment.mailTemplate.query.service;

import com.piveguyz.empickbackend.employment.mailTemplate.query.dto.MailTemplateQueryDTO;

import java.util.List;

public interface MailTemplateQueryService {
    List<MailTemplateQueryDTO> findAll();

    MailTemplateQueryDTO findById(Integer id);

    MailTemplateQueryDTO findByTitle(String title);

    List<MailTemplateQueryDTO> searchByTitle(String title);
}
