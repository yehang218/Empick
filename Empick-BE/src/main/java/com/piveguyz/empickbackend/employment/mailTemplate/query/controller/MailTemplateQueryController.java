package com.piveguyz.empickbackend.employment.mailTemplate.query.controller;

import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.mailTemplate.query.dto.MailTemplateQueryDTO;
import com.piveguyz.empickbackend.employment.mailTemplate.query.service.MailTemplateQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "안내 메일 템플릿 API", description = "안내 메일 템플릿 관리")
@RestController
@RequestMapping("/api/v1/employment/mail-template")
public class MailTemplateQueryController {
    private final MailTemplateQueryService mailTemplateQueryService;

    @Autowired
    public MailTemplateQueryController(MailTemplateQueryService mailTemplateQueryService) {
        this.mailTemplateQueryService = mailTemplateQueryService;
    }

    @Operation(
            summary = "안내 메일 템플릿 조회",
            description = """
    - 안내 메일 템플릿을 조회합니다.
    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
    })
    @GetMapping
    public ResponseEntity<CustomApiResponse<List<MailTemplateQueryDTO>>> findAll() {
        List<MailTemplateQueryDTO> mailTemplateQueryDTOList = mailTemplateQueryService.findAll();
        ResponseCode result = ResponseCode.SUCCESS;
        return ResponseEntity.status(result.getHttpStatus())
                .body(CustomApiResponse.of(result, mailTemplateQueryDTOList));
    }

    @Operation(
            summary = "안내 메일 템플릿 id로 조회",
            description = """
    - 안내 메일 템플릿을 id로 조회합니다.
    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "2603", description = "존재하지 않는 템플릿입니다.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<MailTemplateQueryDTO>> findById(@PathVariable("id") Integer id){
        MailTemplateQueryDTO mailTemplateQueryDTO = mailTemplateQueryService.findById(id);
        ResponseCode result = ResponseCode.SUCCESS;
        return ResponseEntity.status(result.getHttpStatus())
                .body(CustomApiResponse.of(result, mailTemplateQueryDTO));
    }

//    @GetMapping("/title")
//    public ResponseEntity<CustomApiResponse<MailTemplateQueryDTO>> findByTitle(@RequestParam("title") String title){
//        MailTemplateQueryDTO mailTemplateQueryDTO = mailTemplateQueryService.findByTitle(title);
//        ResponseCode result = ResponseCode.SUCCESS;
//        return ResponseEntity.status(result.getHttpStatus())
//                .body(CustomApiResponse.of(result, mailTemplateQueryDTO));
//    }

    @Operation(
            summary = "안내 메일 템플릿 검색",
            description = """
    - 안내 메일 템플릿을 검색합니다.
    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @GetMapping("/search")
    public ResponseEntity<CustomApiResponse<List<MailTemplateQueryDTO>>> searchByTitle(@RequestParam("title") String title){
        List<MailTemplateQueryDTO> mailTemplateQueryDTOList = mailTemplateQueryService.searchByTitle(title);
        ResponseCode result = ResponseCode.SUCCESS;
        return ResponseEntity.status(result.getHttpStatus())
                .body(CustomApiResponse.of(result, mailTemplateQueryDTOList));
    }


}
