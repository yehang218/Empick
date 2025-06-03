package com.piveguyz.empickbackend.employment.mailTemplate.command.application.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.mailTemplate.command.application.dto.MailTemplateCommandDTO;
import com.piveguyz.empickbackend.employment.mailTemplate.command.application.service.MailTemplateCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "안내 메일 템플릿 등록/수정/삭제",
            description = """
    - 안내 메일 템플릿을 관리합니다.
    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "2600", description = "이름이 중복된 템플릿이 존재합니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "2601", description = "제목을 입력하지 않았습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "2602", description = "내용을 입력하지 않았습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "2603", description = "존재하지 않는 템플릿입니다.")
    })

    @PostMapping("/create")
    public ResponseEntity<CustomApiResponse<MailTemplateCommandDTO>> createTemplate(@RequestBody MailTemplateCommandDTO mailTemplateCommandDTO) {
        MailTemplateCommandDTO createdMailTemplateCommandDTO = mailTemplateCommandService.createTemplate(mailTemplateCommandDTO);
        ResponseCode result = ResponseCode.SUCCESS;
        return ResponseEntity.status(result.getHttpStatus())
                .body(CustomApiResponse.of(result, createdMailTemplateCommandDTO));
    }

    @PostMapping("/update")
    public ResponseEntity<CustomApiResponse<MailTemplateCommandDTO>> updateTemplate(@RequestParam("id") Integer id,
                                                                                    @RequestBody MailTemplateCommandDTO mailTemplateCommandDTO) {
        MailTemplateCommandDTO updatedMailTemplateCommandDTO = mailTemplateCommandService.updateTemplate(id, mailTemplateCommandDTO);
        ResponseCode result = ResponseCode.SUCCESS;
        return ResponseEntity.status(result.getHttpStatus())
                .body(CustomApiResponse.of(result, updatedMailTemplateCommandDTO));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<CustomApiResponse<MailTemplateCommandDTO>> deleteTemplate(@RequestParam("id") Integer id) {
        MailTemplateCommandDTO deletedMailTemplateCommandDTO = mailTemplateCommandService.deleteTemplate(id);
        ResponseCode result = ResponseCode.SUCCESS;
        return ResponseEntity.status(result.getHttpStatus())
                .body(CustomApiResponse.of(result, deletedMailTemplateCommandDTO));
    }
}
