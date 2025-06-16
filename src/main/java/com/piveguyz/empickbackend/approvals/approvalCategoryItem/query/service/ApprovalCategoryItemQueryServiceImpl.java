package com.piveguyz.empickbackend.approvals.approvalCategoryItem.query.service;

import com.piveguyz.empickbackend.approvals.approvalCategoryItem.query.dto.ApprovalCategoryItemQueryDTO;
import com.piveguyz.empickbackend.approvals.approvalCategoryItem.query.mapper.ApprovalCategoryItemQueryMapper;
import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalCategoryItemQueryServiceImpl implements ApprovalCategoryItemQueryService {
	private final ApprovalCategoryItemQueryMapper mapper;

	@Override
	public List<ApprovalCategoryItemQueryDTO> findAll() {
		List<ApprovalCategoryItemQueryDTO> dtoList = mapper.findAll();
		return dtoList;
	}

	@Override
	public ApprovalCategoryItemQueryDTO findById(Integer id) {
		ApprovalCategoryItemQueryDTO dto = mapper.findById(id);
		if(dto == null) {
			throw new BusinessException(ResponseCode.NOT_FOUND);
		}
		return dto;
	}

	@Override
	public List<ApprovalCategoryItemQueryDTO> findByCategoryId(Integer categoryId) {
		List<ApprovalCategoryItemQueryDTO> dtoList = mapper.findByCategoryId(categoryId);
		return dtoList;
	}
}
