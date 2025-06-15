package com.piveguyz.empickbackend.approvals.approvalCategoryItem.command.application.controller;

import com.piveguyz.empickbackend.approvals.approvalCategoryItem.command.application.dto.ApprovalCategoryItemCommandDTO;
import com.piveguyz.empickbackend.approvals.approvalCategoryItem.command.application.service.ApprovalCategoryItemCommandService;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.interviews.facade.InterviewFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name="면접 API", description="면접 관리")
@RestController
@RequestMapping("/api/v1/employment/interview")
public class ApprovalCategoryItemCommandController {
    private final ApprovalCategoryItemCommandService service;
    private final InterviewFacade facade;

    @Autowired
    public ApprovalCategoryItemCommandController(ApprovalCategoryItemCommandService service, InterviewFacade facade) {
        this.service = service;
        this.facade = facade;
    }

    @Operation(
            summary = "면접 생성",
            description = """
                    - 면접 일정을 새로 등록합니다.
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @PostMapping
    public ResponseEntity<CustomApiResponse<ApprovalCategoryItemCommandDTO>> create(@RequestBody ApprovalCategoryItemCommandDTO dto) {
        ApprovalCategoryItemCommandDTO createdDTO = facade.createInterview(dto);
        ResponseCode result = ResponseCode.SUCCESS;
        return ResponseEntity.status(result.getHttpStatus())
                .body(CustomApiResponse.of(result, createdDTO));
    }

    @Operation(
            summary = "면접 수정",
            description = """
                    - 면접 일정을 수정합니다.
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CustomApiResponse<ApprovalCategoryItemCommandDTO>> update(@PathVariable("id") Integer id,
                                                                                    @RequestBody ApprovalCategoryItemCommandDTO dto) {
        ApprovalCategoryItemCommandDTO updatedDTO = service.update(id, dto);
        ResponseCode result = ResponseCode.SUCCESS;
        return ResponseEntity.status(result.getHttpStatus())
                .body(CustomApiResponse.of(result, updatedDTO));
    }

    @PatchMapping("/{id}/datetime")
    public ResponseEntity<CustomApiResponse<ApprovalCategoryItemCommandDTO>> updateDateTime(@PathVariable("id") Integer id,
                                                                                            @RequestParam("datetime")
                                                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                                 LocalDateTime datetime){
        ApprovalCategoryItemCommandDTO updatedDTO = service.updateDateTime(id, datetime);
        ResponseCode result = ResponseCode.SUCCESS;
        return ResponseEntity.status(result.getHttpStatus())
                .body(CustomApiResponse.of(result, updatedDTO));
    }

    @PatchMapping("/{id}/address")
    public ResponseEntity<CustomApiResponse<ApprovalCategoryItemCommandDTO>> updateAddress(@PathVariable("id") Integer id,
                                                                                           @RequestParam("address") String address) {
        ApprovalCategoryItemCommandDTO updatedDTO = service.updateAddress(id, address);
        ResponseCode result = ResponseCode.SUCCESS;
        return ResponseEntity.status(result.getHttpStatus())
                .body(CustomApiResponse.of(result, updatedDTO));
    }

    @Operation(
            summary = "면접 삭제",
            description = """
                    - 면접 일정을 삭제합니다.
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<ApprovalCategoryItemCommandDTO>> delete(@PathVariable("id") Integer id) {
        ApprovalCategoryItemCommandDTO deletedDTO = facade.deleteInterview(id);
        ResponseCode result = ResponseCode.SUCCESS;
        return ResponseEntity.status(result.getHttpStatus())
                .body(CustomApiResponse.of(result, deletedDTO));
    }
}
