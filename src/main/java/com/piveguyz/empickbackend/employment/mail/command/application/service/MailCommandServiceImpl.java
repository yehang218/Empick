package com.piveguyz.empickbackend.employment.mail.command.application.service;

import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.mail.command.application.dto.MailCommandDTO;
import com.piveguyz.empickbackend.employment.mail.command.domain.aggregate.MailEntity;
import com.piveguyz.empickbackend.employment.mail.command.domain.repository.MailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailCommandServiceImpl implements MailCommandService {
    private final MailRepository mailRepository;
    private ModelMapper modelMapper;

    @Override
    public ResponseCode createMail(MailCommandDTO mailCommandDTO) {
        MailEntity mailEntity = modelMapper.map(mailCommandDTO, MailEntity.class);
        try {
            mailRepository.save(mailEntity);
            return ResponseCode.SUCCESS;
        } catch (Exception e) {
            return ResponseCode.BAD_REQUEST;
        }
    }
}
