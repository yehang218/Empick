package com.piveguyz.empickbackend.mail.query.service;

import com.piveguyz.empickbackend.mail.query.dto.MailQueryDTO;
import com.piveguyz.empickbackend.mail.query.mapper.MailMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailQueryServiceImpl implements MailQueryService {
    private final MailMapper mailMapper;
    @Override
    public List<MailQueryDTO> findAll() {
        List<MailQueryDTO> mailQueryDTOList = mailMapper.findAll();
        return mailQueryDTOList;
    }

    @Override
    public MailQueryDTO findById(Integer id) {
        MailQueryDTO mailQueryDTO = mailMapper.findById();
        return mailQueryDTO;
    }

    @Override
    public List<MailQueryDTO> findByEmail(String email) {
        List<MailQueryDTO> mailQueryDTOList = mailMapper.findByEmail();
        return mailQueryDTOList;
    }
}
