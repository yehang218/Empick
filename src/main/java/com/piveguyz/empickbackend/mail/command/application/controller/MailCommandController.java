package com.piveguyz.empickbackend.mail.command.application.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.mail.command.application.dto.MailCommandDTO;
import com.piveguyz.empickbackend.mail.command.application.service.MailCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/employment/mail")
public class MailCommandController {
    private final MailCommandService mailCommandService;

    public MailCommandController(MailCommandService mailCommandService) {
        this.mailCommandService = mailCommandService;
    }

    @PostMapping("/create")
    public ResponseEntity<CustomApiResponse<MailCommandDTO>> createMail(@RequestBody MailCommandDTO mailCommandDTO) {
        MailCommandDTO createdMailCommandDTO = mailCommandService.createMail(mailCommandDTO);
        return ResponseEntity
    }
}
