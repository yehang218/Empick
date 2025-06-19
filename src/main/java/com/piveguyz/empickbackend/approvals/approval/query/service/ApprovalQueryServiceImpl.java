package com.piveguyz.empickbackend.approvals.approval.query.service;

import com.piveguyz.empickbackend.approvals.approval.query.dto.ApprovalQueryDTO;
import com.piveguyz.empickbackend.approvals.approval.query.dto.ApprovalRequestedListQueryDTO;
import com.piveguyz.empickbackend.approvals.approval.query.mapper.ApprovalQueryMapper;
import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalQueryServiceImpl implements ApprovalQueryService {
	private final ApprovalQueryMapper mapper;

	@Override
	public List<ApprovalQueryDTO> findAll() {
		List<ApprovalQueryDTO> dtoList = mapper.findAll();
		return dtoList;
	}

	@Override
	public ApprovalQueryDTO findById(Integer id) {
		ApprovalQueryDTO dto = mapper.findById(id);
		if(dto == null){
			throw new BusinessException(ResponseCode.NOT_FOUND);
		}
		return dto;
	}

	@Override
	public List<ApprovalQueryDTO> findByCategoryId(Integer categoryId) {
		List<ApprovalQueryDTO> dtoList = mapper.findByCategoryId(categoryId);
		return dtoList;
	}

	@Override
	public List<ApprovalQueryDTO> findByWriterId(Integer writerId) {
		List<ApprovalQueryDTO> dtoList = mapper.findByWriterId(writerId);
		return dtoList;
	}

	@Override
	public List<ApprovalQueryDTO> findReceivedApprovals(Integer memberId) {
		return mapper.findReceivedApprovals(memberId);
	}

	@Override
	public List<ApprovalRequestedListQueryDTO> findRequestedApprovals(Integer memberId) {
		return mapper.findRequestedApprovals(memberId);
	}
}
