package com.piveguyz.empickbackend.employment.mail.facade;

import com.piveguyz.empickbackend.employment.applicant.command.domain.repository.ApplicantRepository;
import com.piveguyz.empickbackend.employment.applicant.query.service.ApplicantQueryService;
import com.piveguyz.empickbackend.employment.mail.command.application.dto.MailCommandDTO;
import com.piveguyz.empickbackend.employment.mail.command.application.service.MailCommandService;
import com.piveguyz.empickbackend.employment.mail.query.dto.InterviewMailDTO;
import com.piveguyz.empickbackend.employment.mail.query.mapper.MailMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class MailFacade {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final MailMapper mailMapper;
    private final MailCommandService mailCommandService;

    public MailCommandDTO sendJobtestMail(MailCommandDTO dto) {
        String email = dto.getEmail();
        Integer applicantId = dto.getApplicantId();
        if(applicantId != null) {

        }
            try {
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

                Context context = new Context();
                context.setVariable("applicantName", "홍길동");
                context.setVariable("jobTitle", "백엔드 개발자");
                context.setVariable("testDate", "2025년 6월 20일 (목) 오전 10시");
                context.setVariable("testLocation", "서울시 강남구 역삼로 123, 5층");
                context.setVariable("detailUrl", "https://your-domain.com/test-detail/123");

                String base64Image = getBase64EncodedImage("static/images/empick-logo.png");
                context.setVariable("logoImage", base64Image);

                String htmlContent = templateEngine.process("jobtest-mail-template", context);
                mimeMessageHelper.setTo(email);
                mimeMessageHelper.setSubject(dto.getTitle());
                mimeMessageHelper.setText(htmlContent, true);
                javaMailSender.send(mimeMessage);
                System.out.println("Thymeleaf 템플릿 이메일 전송 성공!");
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        return dto;
    }

    public MailCommandDTO sendInterviewMail(Integer applicationId) {
        InterviewMailDTO dto = mailMapper.findForInterviewMail(applicationId);
        Integer recruitmentId = dto.getRecruitmentId();
        String recruitmentTitle = dto.getRecruitmentTitle();
        Integer applicantId = dto.getApplicantId();
        String applicantName = dto.getApplicantName();
        String email = dto.getEmail();
        Integer interviewId = dto.getInterviewId();
        LocalDateTime interviewDateTime = dto.getInterviewDatetime();
        String interviewAddress = dto.getInterviewAddress();
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
            String subject = "면접 일정 안내 메일";

//            mimeMessageHelper.setTo(email);   // 현재 DB상 이메일이라 확인 불가
            mimeMessageHelper.setTo("tjalswhd1@naver.com"); // 확인을 위해 본인 이메일 사용
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
            System.out.println("Thymeleaf 템플릿 이메일 전송 성공!");

            MailCommandDTO sendedDTO = new MailCommandDTO();
            sendedDTO.setEmail(email);
            sendedDTO.setApplicantId(applicantId);
            sendedDTO.setTitle(subject);
            sendedDTO.setContent(htmlContent);
            sendedDTO.setSendedAt(LocalDateTime.now());
            mailCommandService.createMail(sendedDTO);
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
