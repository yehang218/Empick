package com.piveguyz.empickbackend.employment.mail.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.mail.command.application.dto.MailCommandDTO;
import com.piveguyz.empickbackend.employment.mail.command.application.mapper.MailCommandMapper;
import com.piveguyz.empickbackend.employment.mail.command.domain.aggregate.MailEntity;
import com.piveguyz.empickbackend.employment.mail.command.domain.repository.MailRepository;
import com.piveguyz.empickbackend.employment.mail.query.dto.InterviewMailQueryDTO;
import com.piveguyz.empickbackend.employment.mail.query.dto.JobTestMailQueryDTO;
import com.piveguyz.empickbackend.employment.mail.query.mapper.MailMapper;
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
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailCommandServiceImpl implements MailCommandService {
    private final MailRepository mailRepository;
    private final MailCommandMapper mailCommandMapper;
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final MailMapper mailMapper;

    @Override
    public MailCommandDTO createMail(MailCommandDTO dto) {
        String title = dto.getTitle();
        if (title == null || title.isEmpty()) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_MAIL_NO_TITLE);
        }
        String content = dto.getContent();
        if (content == null || content.isEmpty()) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_MAIL_NO_CONTENT);
        }
        String email = dto.getEmail();
        if (!email.contains("@")) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_MAIL_INADEQUATE_EMAIL);
        }
        MailEntity entity = new MailEntity();
        entity.setApplicantId(dto.getApplicantId());
        entity.setEmail(email);
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setSenderId(1);
        entity.setSendedAt(LocalDateTime.now());
        MailEntity savedEntity = mailRepository.save(entity);
        return mailCommandMapper.toDTO(savedEntity);
    }

    @Async
    public MailCommandDTO sendSimpleMail(MailCommandDTO dto) {
        String email = dto.getEmail();
        if(!email.contains("@")) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_MAIL_INADEQUATE_EMAIL);
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(dto.getEmail());
        message.setFrom("noreply@example.com");
        message.setSubject(dto.getTitle());
        message.setText(dto.getContent());
        javaMailSender.send(message);
        return dto;
    }

    @Override
    public MailCommandDTO sendHTMLMail(MailCommandDTO dto) {
        String email = dto.getEmail();
        if(!email.contains("@")) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_MAIL_INADEQUATE_EMAIL);
        }
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//            mimeMessageHelper.setFrom("noreply@example.com");
//            mimeMessageHelper.setTo(dto.getEmail());
//            mimeMessageHelper.setSubject(dto.getTitle());
//            mimeMessageHelper.setText(dto.getContent(), true);

            Context context = new Context();
            context.setVariable("subject", dto.getTitle());
            context.setVariable("message", dto.getContent());
            String base64Image = getBase64EncodedImage("static/images/empick-logo.png");
            context.setVariable("logoImage", base64Image);
            String htmlContent = templateEngine.process("email-template", context);

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(dto.getTitle());
            mimeMessageHelper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);

            MailCommandDTO sendedDTO = new MailCommandDTO();
            sendedDTO.setEmail(email);
            sendedDTO.setTitle(dto.getTitle());
            sendedDTO.setContent(htmlContent);
            sendedDTO.setSenderId(1);
            sendedDTO.setSendedAt(LocalDateTime.now());
            return sendedDTO;
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MailCommandDTO sendJobtestMail(Integer id) {
        JobTestMailQueryDTO dto = mailMapper.findForJobTestMail(id);
        Integer recruitmentId = dto.getRecruitmentId();
        String recruitmentTitle = dto.getRecruitmentTitle();
        Integer applicantId = dto.getApplicantId();
        String applicantName = dto.getApplicantName();
        String email = dto.getEmail();
        Integer applicationJobTestId = dto.getApplicationJobTestId();
        String entryCode = dto.getEntryCode();
        Integer jobTestId = dto.getJobTestId();
        String jobTestTitle = dto.getJobTestTitle();
        Integer testTime = dto.getTestTime();
        LocalDateTime startedAt = dto.getStartedAt();
        LocalDateTime endedAt = dto.getEndedAt();
        Integer problemCount = dto.getProblemCount();
        if(applicantId == null) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_APPLICANT_NOT_FOUND);
        }
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            Context context = new Context();
            context.setVariable("recruitmentTitle", recruitmentTitle);
            context.setVariable("applicantName", applicantName);
            context.setVariable("entryCode", entryCode);
            context.setVariable("jobTestTitle", jobTestTitle);
            context.setVariable("testTime", testTime);
            context.setVariable("startedAt", startedAt);
            context.setVariable("endedAt", endedAt);
            context.setVariable("problemCount", problemCount);

            String base64Image = getBase64EncodedImage("static/images/empick-logo.png");
            context.setVariable("logoImage", base64Image);

            String htmlContent = templateEngine.process("jobtest-mail-template", context);
            String title = "실무 테스트 일정 안내 메일";

//            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setTo("tjalswhd1@naver.com");
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);

            MailCommandDTO sendedDTO = new MailCommandDTO();
            sendedDTO.setApplicantId(applicantId);
            sendedDTO.setEmail(email);
            sendedDTO.setTitle(title);
            sendedDTO.setContent(htmlContent);
            sendedDTO.setSenderId(1);
            sendedDTO.setSendedAt(LocalDateTime.now());
            return sendedDTO;
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MailCommandDTO sendInterviewMail(Integer applicationId) {
        InterviewMailQueryDTO dto = mailMapper.findForInterviewMail(applicationId);
        Integer recruitmentId = dto.getRecruitmentId();
        String recruitmentTitle = dto.getRecruitmentTitle();
        Integer applicantId = dto.getApplicantId();
        String applicantName = dto.getApplicantName();
        String email = dto.getEmail();
        Integer interviewId = dto.getInterviewId();
        LocalDateTime interviewDateTime = dto.getInterviewDatetime();
        String interviewAddress = dto.getInterviewAddress();
        if(applicantId == null) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_APPLICANT_NOT_FOUND);
        }
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 (E) a h시 mm분")
                    .withLocale(Locale.KOREAN);
            String formattedDateTime = interviewDateTime.format(formatter);

            Context context = new Context();
            context.setVariable("recruitmentTitle", recruitmentTitle);
            context.setVariable("applicantName", applicantName);
            context.setVariable("interviewDateTimeFormatted", formattedDateTime);
            context.setVariable("zoomUrl", interviewAddress);

            String base64Image = getBase64EncodedImage("static/images/empick-logo.png");
            context.setVariable("logoImage", base64Image);

            String htmlContent = templateEngine.process("interview-mail-template", context);
            String title = "면접 일정 안내 메일";

//            mimeMessageHelper.setTo(email);   // 현재 DB상 이메일이라 확인 불가
            mimeMessageHelper.setTo("tjalswhd1@naver.com"); // 확인을 위해 본인 이메일 사용
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
            System.out.println("Thymeleaf 템플릿 이메일 전송 성공!");

            MailCommandDTO sendedDTO = new MailCommandDTO();
            sendedDTO.setApplicantId(applicantId);
            sendedDTO.setEmail(email);
            sendedDTO.setTitle(title);
            sendedDTO.setContent(htmlContent);
            sendedDTO.setSenderId(1);
            sendedDTO.setSendedAt(LocalDateTime.now());
            return sendedDTO;
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getBase64EncodedImage(String imagePath) throws IOException {
        InputStream is = new ClassPathResource(imagePath).getInputStream();
        byte[] imageBytes = is.readAllBytes();
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}