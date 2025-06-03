package com.piveguyz.empickbackend.employment.mail.query.service;

import com.piveguyz.empickbackend.employment.mail.query.dto.MailQueryDTO;

import java.util.List;

public interface MailQueryService {
    List<MailQueryDTO> findAll();

    MailQueryDTO findById(Integer id);

    List<MailQueryDTO> findByEmail(String email);
}
