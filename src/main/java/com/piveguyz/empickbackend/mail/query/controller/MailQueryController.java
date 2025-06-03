package com.piveguyz.empickbackend.mail.query.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.mail.query.dto.MailQueryDTO;
import com.piveguyz.empickbackend.mail.query.service.MailQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/employment/mail")
public class MailQueryController {
    private final MailQueryService mailQueryService;

    @Autowired
    public MailQueryController(MailQueryService mailQueryService) {
        this.mailQueryService = mailQueryService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<CustomApiResponse<List<MailQueryDTO>>> findAll() {
        try {
            List<MailQueryDTO> mailQueryDTOList = mailQueryService.findAll();
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(CustomApiResponse.of(ResponseCode.SUCCESS, mailQueryDTOList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(CustomApiResponse.of(ResponseCode.NOT_FOUND, null));
        }
    }

    @GetMapping("/findById")
    public ResponseEntity<CustomApiResponse<MailQueryDTO>> findById(@RequestParam("id") Integer id){
        try {
            MailQueryDTO mailQueryDTO = mailQueryService.findById(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(CustomApiResponse.of(ResponseCode.SUCCESS, mailQueryDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(CustomApiResponse.of(ResponseCode.NOT_FOUND, null));
        }
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<CustomApiResponse<List<MailQueryDTO>>> findByEmail(@RequestParam("email") String email){
        try {
            List<MailQueryDTO> mailQueryDTOList = mailQueryService.findByEmail(email);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(CustomApiResponse.of(ResponseCode.SUCCESS, mailQueryDTOList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(CustomApiResponse.of(ResponseCode.NOT_FOUND, null));
        }
    }
}
