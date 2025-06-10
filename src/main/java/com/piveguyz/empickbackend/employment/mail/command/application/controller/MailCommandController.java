package com.piveguyz.empickbackend.employment.mail.command.application.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.mail.command.application.dto.MailCommandDTO;
import com.piveguyz.empickbackend.employment.mail.command.application.service.MailCommandService;
import com.piveguyz.empickbackend.employment.mail.facade.MailFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "안내 메일 API", description = "안내 메일 관리")
@RestController
@RequestMapping("/api/v1/employment/mail")
public class MailCommandController {
    private final MailCommandService mailCommandService;
    private final MailFacade mailFacade;

    @Autowired
    public MailCommandController(MailCommandService mailCommandService, MailFacade mailFacade) {
        this.mailCommandService = mailCommandService;
        this.mailFacade = mailFacade;
    }

    @Operation(
            summary = "안내 메일 등록",
            description = """
    - 안내 메일을 관리합니다.
    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "2631", description = "메일의 내용을 입력하지 않았습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "2632", description = "유효하지 않은 형태의 이메일입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "2633", description = "메일의 제목을 입력하지 않았습니다."),
    })

    @PostMapping("/create")
    public ResponseEntity<CustomApiResponse<MailCommandDTO>> createMail(@RequestBody MailCommandDTO mailCommandDTO) {
        MailCommandDTO createdDTO = mailCommandService.createMail(mailCommandDTO);       // DB에 메일 저장
        ResponseCode result = ResponseCode.SUCCESS;
        return ResponseEntity.status(result.getHttpStatus())
                .body(CustomApiResponse.of(result, createdDTO));
    }

    @PostMapping("/send")
    public ResponseEntity<CustomApiResponse<MailCommandDTO>> sendMail(@RequestBody MailCommandDTO mailCommandDTO) {
        MailCommandDTO sendedDTO = mailFacade.sendMail(mailCommandDTO);
        MailCommandDTO createdDTO = mailCommandService.createMail(sendedDTO);
        ResponseCode result = ResponseCode.SUCCESS;
        return ResponseEntity.status(result.getHttpStatus())
                .body(CustomApiResponse.of(result, createdDTO));

    }

    @PostMapping("/send/jobtest/{id}")
    public ResponseEntity<CustomApiResponse<MailCommandDTO>> sendJobtestMail(@PathVariable("id") Integer id) {
        MailCommandDTO sendedDTO = mailFacade.sendJobtestMail(id);
        ResponseCode result = ResponseCode.SUCCESS;
        return ResponseEntity.status(result.getHttpStatus())
                .body(CustomApiResponse.of(result, sendedDTO));
    }

    @PostMapping("/send/interview/{id}")
    public ResponseEntity<CustomApiResponse<MailCommandDTO>> sendInterviewMail(@PathVariable("id") Integer id) {
        MailCommandDTO sendedDTO = mailFacade.sendInterviewMail(id);
        ResponseCode result = ResponseCode.SUCCESS;
        return ResponseEntity.status(result.getHttpStatus())
                .body(CustomApiResponse.of(result, sendedDTO));
    }
}
