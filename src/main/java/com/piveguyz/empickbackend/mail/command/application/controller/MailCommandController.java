package com.piveguyz.empickbackend.mail.command.application.controller;

import com.piveguyz.empickbackend.common.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<MailCommandDTO>> createMail(@RequestBody MailCommandDTO mailCommandDTO) {
        ResponseCode result = mailCommandService.createMail(mailCommandDTO);
        if(result.isSuccess()){
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.of(result, mailCommandDTO));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.of(result, null));
        }
    }
}
