package com.piveguyz.empickbackend.employment.mail.facade;

import com.piveguyz.empickbackend.employment.applicant.command.domain.repository.ApplicantRepository;
import com.piveguyz.empickbackend.employment.applicant.query.service.ApplicantQueryService;
import com.piveguyz.empickbackend.employment.mail.command.application.dto.MailCommandDTO;
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

    public MailCommandDTO sendInterviewMail(MailCommandDTO dto) {
        String email = dto.getEmail();
        Integer applicantId = dto.getApplicantId();
        String applicantName = null;
        if(applicantId != null) {
//            applicantName = ApplicantRepository.findById(applicantId);
        }
            try {
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

                LocalDateTime interviewDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 (E) a h시 mm분")
                        .withLocale(Locale.KOREAN);
                String formattedDateTime = interviewDateTime.format(formatter);

                Context context = new Context();
                context.setVariable("applicantName", "홍길동");
                context.setVariable("jobTitle", dto.getTitle());
                context.setVariable("interviewDateTimeFormatted", formattedDateTime);
                context.setVariable("zoomUrl", "https://zoom.us/j/your-meeting-id");

                String base64Image = getBase64EncodedImage("static/images/empick-logo.png");
                context.setVariable("logoImage", base64Image);

                String htmlContent = templateEngine.process("interview-mail-template", context);
                mimeMessageHelper.setTo(email);
                mimeMessageHelper.setSubject(dto.getTitle());
                mimeMessageHelper.setText(htmlContent, true);
                javaMailSender.send(mimeMessage);
                System.out.println("Thymeleaf 템플릿 이메일 전송 성공!");
            } catch (MessagingException | IOException e) {
                throw new RuntimeException(e);
            }
        return dto;
    }

    public String getBase64EncodedImage(String imagePath) throws IOException {
        InputStream is = new ClassPathResource(imagePath).getInputStream();
        byte[] imageBytes = is.readAllBytes();
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
