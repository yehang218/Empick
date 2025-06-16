package com.piveguyz.empickbackend.approvals.approvalContent.query.service;

import com.piveguyz.empickbackend.approvals.approvalContent.query.dto.ApprovalContentQueryDTO;
import com.piveguyz.empickbackend.approvals.approvalContent.query.mapper.ApprovalContentQueryMapper;
import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalContentQueryServiceImpl implements ApprovalContentQueryService {
	private final ApprovalContentQueryMapper mapper;

	@Override
	public List<ApprovalContentQueryDTO> findAll() {
		List<ApprovalContentQueryDTO> dtoList = mapper.findAll();
		return dtoList;
	}

	@Override
	public ApprovalContentQueryDTO findById(Integer id) {
		ApprovalContentQueryDTO dto = mapper.findById(id);
		if(dto == null) {
			throw new BusinessException(ResponseCode.NOT_FOUND);
		}
		return dto;
	}

	@Override
	public List<ApprovalContentQueryDTO> findByApprovalId(Integer approvalId) {
		List<ApprovalContentQueryDTO> dtoList = mapper.findByApprovalId(approvalId);
		return dtoList;
	}
}
