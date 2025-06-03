package com.piveguyz.empickbackend.employment.mail.query.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.mail.query.dto.MailQueryDTO;
import com.piveguyz.empickbackend.employment.mail.query.service.MailQueryService;
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
        List<MailQueryDTO> mailQueryDTOList = mailQueryService.findAll();
        ResponseCode result = ResponseCode.SUCCESS;
        return ResponseEntity.status(result.getHttpStatus())
                .body(CustomApiResponse.of(result, mailQueryDTOList));
    }

    @GetMapping("/findById")
    public ResponseEntity<CustomApiResponse<MailQueryDTO>> findById(@RequestParam("id") Integer id){
        MailQueryDTO mailQueryDTO = mailQueryService.findById(id);
        ResponseCode result = ResponseCode.SUCCESS;
        return ResponseEntity.status(result.getHttpStatus())
                .body(CustomApiResponse.of(result, mailQueryDTO));
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<CustomApiResponse<List<MailQueryDTO>>> findByEmail(@RequestParam("email") String email){
        List<MailQueryDTO> mailQueryDTOList = mailQueryService.findByEmail(email);
        ResponseCode result = ResponseCode.SUCCESS;
        return ResponseEntity.status(result.getHttpStatus())
                .body(CustomApiResponse.of(result, mailQueryDTOList));
    }
}
