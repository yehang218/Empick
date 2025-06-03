package com.piveguyz.empickbackend.mailTemplate.query.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.mailTemplate.query.dto.MailTemplateQueryDTO;
import com.piveguyz.empickbackend.mailTemplate.query.service.MailTemplateQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employment/mailTemplate")
public class MailTemplateQueryController {
    private final MailTemplateQueryService mailTemplateQueryService;

    @Autowired
    public MailTemplateQueryController(MailTemplateQueryService mailTemplateQueryService) {
        this.mailTemplateQueryService = mailTemplateQueryService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<CustomApiResponse<List<MailTemplateQueryDTO>>> findAll() {
        try {
            List<MailTemplateQueryDTO> mailTemplateQueryDTOList = mailTemplateQueryService.findAll();
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(CustomApiResponse.of(ResponseCode.SUCCESS, mailTemplateQueryDTOList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(CustomApiResponse.of(ResponseCode.NOT_FOUND, null));
        }
    }

    @GetMapping("/findByTitle")
    public ResponseEntity<CustomApiResponse<MailTemplateQueryDTO>> findByTitle(@RequestParam("title") String title){
        try {
            MailTemplateQueryDTO mailTemplateQueryDTO = mailTemplateQueryService.findByTitle(title);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(CustomApiResponse.of(ResponseCode.SUCCESS, mailTemplateQueryDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(CustomApiResponse.of(ResponseCode.NOT_FOUND, null));
        }
    }

    @GetMapping("/searchByTitle")
    public ResponseEntity<CustomApiResponse<List<MailTemplateQueryDTO>>> searchByTitle(@RequestParam("title") String title){
        try {
            List<MailTemplateQueryDTO> mailTemplateQueryDTOList = mailTemplateQueryService.searchByTitle(title);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(CustomApiResponse.of(ResponseCode.SUCCESS, mailTemplateQueryDTOList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(CustomApiResponse.of(ResponseCode.NOT_FOUND, null));
        }
    }

    @GetMapping("/findById")
    public ResponseEntity<CustomApiResponse<MailTemplateQueryDTO>> findById(@RequestParam("id") Integer id){
        try {
            MailTemplateQueryDTO mailTemplateQueryDTO = mailTemplateQueryService.findById(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(CustomApiResponse.of(ResponseCode.SUCCESS, mailTemplateQueryDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(CustomApiResponse.of(ResponseCode.NOT_FOUND, null));
        }
    }
}
