package com.piveguyz.empickbackend.employment.mail.facade;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.mail.command.application.dto.MailCommandDTO;
import com.piveguyz.empickbackend.employment.mail.command.application.service.MailCommandService;
import com.piveguyz.empickbackend.employment.mail.query.dto.InterviewMailQueryDTO;
import com.piveguyz.empickbackend.employment.mail.query.dto.JobTestMailQueryDTO;
import com.piveguyz.empickbackend.employment.mail.query.mapper.MailMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;

@Service
@AllArgsConstructor
public class MailFacade {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final MailMapper mailMapper;
    private final MailCommandService mailCommandService;

    public MailCommandDTO sendMail(MailCommandDTO dto) {
        MailCommandDTO sendedDTO = mailCommandService.sendSimpleMail(dto);
        MailCommandDTO createdDTO = mailCommandService.createMail(sendedDTO);
        return createdDTO;
    }

    public MailCommandDTO sendJobtestMail(Integer applicationId, Integer senderId) {
        MailCommandDTO sendedDTO = mailCommandService.sendJobtestMail(applicationId, senderId);
        MailCommandDTO createdDTO = mailCommandService.createMail(sendedDTO);
        return createdDTO;
    }

    public MailCommandDTO sendInterviewMail(Integer applicationId, Integer senderId) {
        MailCommandDTO sendedDTO = mailCommandService.sendInterviewMail(applicationId, senderId);
        MailCommandDTO createdDTO = mailCommandService.createMail(sendedDTO);
        return createdDTO;
    }
}
