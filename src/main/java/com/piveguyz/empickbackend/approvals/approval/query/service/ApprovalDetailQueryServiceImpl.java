package com.piveguyz.empickbackend.approvals.approval.query.service;

import com.piveguyz.empickbackend.approvals.approval.query.dto.*;
import com.piveguyz.empickbackend.approvals.approval.query.mapper.ApprovalQueryMapper;
import com.piveguyz.empickbackend.approvals.approvalCategoryItem.query.dto.ApprovalCategoryItemQueryDTO;
import com.piveguyz.empickbackend.approvals.approvalCategoryItem.query.mapper.ApprovalCategoryItemQueryMapper;
import com.piveguyz.empickbackend.approvals.approvalContent.query.dto.ApprovalContentQueryDTO;
import com.piveguyz.empickbackend.approvals.approvalContent.query.mapper.ApprovalContentQueryMapper;
import com.piveguyz.empickbackend.common.enums.InputType;
import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApprovalDetailQueryServiceImpl implements ApprovalDetailQueryService {

	private final ApprovalQueryMapper approvalQueryMapper;
	private final ApprovalContentQueryMapper contentMapper;
	private final ApprovalCategoryItemQueryMapper itemMapper;

	@Override
	public ApprovalReceivedDetailQueryDTO getReceivedApprovalDetail(Integer approvalId, Integer memberId) {
		ApprovalReceivedDetailQueryDTO basic = approvalQueryMapper.findApprovalBasicDetail(approvalId);
		if (basic == null) {
			throw new BusinessException(ResponseCode.NOT_FOUND);
		}

		List<ApprovalContentQueryDTO> contents = contentMapper.findByApprovalId(approvalId);
		List<ApprovalCategoryItemQueryDTO> items = itemMapper.findByCategoryId(basic.getCategoryId());

		List<ApprovalContentItemDTO> contentItems = contents.stream().map(c -> {
			var matched = items.stream()
				.filter(i -> i.getId().equals(c.getItemId()))
				.findFirst().orElse(null);
			return new ApprovalContentItemDTO(
				matched != null ? matched.getName() : "(알 수 없음)",
				matched != null ? matched.getInputType() : InputType.TEXT,
				c.getContent()
			);
		}).collect(Collectors.toList());

		List<ApprovalLineDetailDTO> approvers = approvalQueryMapper.findApproverDetails(approvalId);
		boolean isMyTurn = approvalQueryMapper.isMyTurn(approvalId, memberId);

		return new ApprovalReceivedDetailQueryDTO(
			basic.getApprovalId(),
			basic.getCategoryId(),
			basic.getCategoryName(),
			basic.getWriterName(),
			basic.getWriterDepartment(),
			basic.getWriterPosition(),
			basic.getCreatedAt(),
			basic.getStatus(),
			contentItems,
			approvers,
			isMyTurn
		);
	}
}
