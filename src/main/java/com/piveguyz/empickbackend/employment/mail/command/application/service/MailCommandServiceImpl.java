package com.piveguyz.empickbackend.employment.mail.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.mail.command.application.dto.MailCommandDTO;
import com.piveguyz.empickbackend.employment.mail.command.application.mapper.MailCommandMapper;
import com.piveguyz.empickbackend.employment.mail.command.domain.aggregate.MailEntity;
import com.piveguyz.empickbackend.employment.mail.command.domain.repository.MailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailCommandServiceImpl implements MailCommandService {
    private final MailRepository mailRepository;
    private final MailCommandMapper mailCommandMapper;
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Override
    public MailCommandDTO createMail(MailCommandDTO dto) {
        String title = dto.getTitle();
        if(title == null || title.isEmpty()) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_MAIL_NO_TITLE);
        }
        String content = dto.getContent();
        if(content == null || content.isEmpty()){
            throw new BusinessException(ResponseCode.EMPLOYMENT_MAIL_NO_CONTENT);
        }
        String email = dto.getEmail();
        if(!email.contains("@")){
            throw new BusinessException(ResponseCode.EMPLOYMENT_MAIL_INADEQUATE_EMAIL);
        }
        MailEntity entity = new MailEntity();
        entity.setApplicantId(dto.getApplicantId());
        entity.setEmail(dto.getEmail());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setSenderId(1);
        entity.setSendedAt(LocalDateTime.now());
        MailEntity savedEntity = mailRepository.save(entity);
        MailCommandDTO savedDTO = mailCommandMapper.toDTO(savedEntity);
        return savedDTO;
    }

    @Async
    public void sendSimpleMail(MailCommandDTO dto){
        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(dto.getEmail());
        message.setFrom("noreply@example.com");
        message.setTo("tjalswhd1@gmail.com");
        message.setSubject(dto.getTitle());
        message.setText(dto.getContent());

        javaMailSender.send(message);

    }

    @Override
    public void sendHTMLMail(MailCommandDTO createdMailCommandDTO) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//            mimeMessageHelper.setFrom("noreply@example.com");
//            mimeMessageHelper.setTo(createdMailCommandDTO.getEmail());
//            mimeMessageHelper.setSubject(createdMailCommandDTO.getTitle());
//            mimeMessageHelper.setText(createdMailCommandDTO.getContent(), true);

            Context context = new Context();
            context.setVariable("subject", createdMailCommandDTO.getTitle());
            context.setVariable("message", createdMailCommandDTO.getContent());
//            if (createdMailCommandDTO.getTarget().equals("user")) {
//                context.setVariable("userType", "일반 사용자");
//            } else if (createdMailCommandDTO.getTarget().equals("admin")) {
//                context.setVariable("userType", "관리자");
//            }

            // MailSendServiceImpl.java 내부
            String base64Image = getBase64EncodedImage("static/images/empick-logo.png");
            context.setVariable("logoImage", base64Image);

            String htmlContent = templateEngine.process("email-template", context);
            mimeMessageHelper.setTo(createdMailCommandDTO.getEmail());
            mimeMessageHelper.setSubject(createdMailCommandDTO.getTitle());
            mimeMessageHelper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
            System.out.println("Thymeleaf 템플릿 이메일 전송 성공!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getBase64EncodedImage(String imagePath) throws IOException {
        InputStream is = new ClassPathResource(imagePath).getInputStream();
        byte[] imageBytes = is.readAllBytes();
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
