package com.piveguyz.empickbackend.mailTemplate.command.application.controller;

import com.piveguyz.empickbackend.common.response.ApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.mailTemplate.command.application.dto.MailTemplateCommandDTO;
import com.piveguyz.empickbackend.mailTemplate.command.application.service.MailTemplateCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employment/mailTemplate")
public class MailTemplateCommandController {
    private final MailTemplateCommandService mailTemplateCommandService;

    @Autowired
    public MailTemplateCommandController(MailTemplateCommandService mailTemplateCommandService) {
        this.mailTemplateCommandService = mailTemplateCommandService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<MailTemplateCommandDTO>> createTemplate(@RequestBody MailTemplateCommandDTO mailTemplateCommandDTO) {
        MailTemplateCommandDTO createdMailTemplateCommandDTO = mailTemplateCommandService.createTemplate(mailTemplateCommandDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(ResponseCode.SUCCESS, createdMailTemplateCommandDTO));
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<MailTemplateCommandDTO>> updateTemplate(@RequestBody MailTemplateCommandDTO mailTemplateCommandDTO) {
        MailTemplateCommandDTO updatedMailTemplateCommandDTO = mailTemplateCommandService.updateTemplate(mailTemplateCommandDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(ResponseCode.SUCCESS, updatedMailTemplateCommandDTO));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<MailTemplateCommandDTO>> deleteTemplate(@RequestParam("id") Integer id) {
        MailTemplateCommandDTO deletedMailTemplateCommandDTO = mailTemplateCommandService.deleteTemplate(id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(ResponseCode.SUCCESS, deletedMailTemplateCommandDTO));
    }
    }
}
