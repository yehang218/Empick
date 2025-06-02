package com.piveguyz.empickbackend.mailTemplate.query.controller;

import com.piveguyz.empickbackend.mailTemplate.query.dto.MailTemplateQueryDTO;
import com.piveguyz.empickbackend.mailTemplate.query.service.MailTemplateQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/mailTemplate")
public class MailTemplateQueryController {
    private final MailTemplateQueryService mailTemplateQueryService;

    @Autowired
    public MailTemplateQueryController(MailTemplateQueryService mailTemplateQueryService) {
        this.mailTemplateQueryService = mailTemplateQueryService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<MailTemplateQueryDTO>> findAll() {
        List<MailTemplateQueryDTO> mailTemplateQueryDTOList = new ArrayList<>();
        mailTemplateQueryDTOList = mailTemplateQueryService.findAll();
        return ResponseEntity.ok(mailTemplateQueryDTOList);
    }

    @GetMapping("/findByTitle")
    public ResponseEntity<List<MailTemplateQueryDTO>> findByTitle(@RequestParam("title") String title){
        List<MailTemplateQueryDTO> mailTemplateQueryDTOList = new ArrayList<>();
        mailTemplateQueryDTOList = mailTemplateQueryService.findByTitle(title);

    }
}
