package com.piveguyz.empickbackend.approvals.approvalCategory.query.service;

import com.piveguyz.empickbackend.approvals.approvalCategory.query.dto.ApprovalCategoryQueryDTO;
import com.piveguyz.empickbackend.approvals.approvalCategory.query.mapper.ApprovalCategoryQueryMapper;
import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalCategoryQueryServiceImpl implements ApprovalCategoryQueryService {
	private final ApprovalCategoryQueryMapper mapper;

	@Override
	public List<ApprovalCategoryQueryDTO> findAll() {
		List<ApprovalCategoryQueryDTO> dtoList = mapper.findAll();
		return dtoList;
	}

	@Override
	public ApprovalCategoryQueryDTO findById(Integer id) {
		ApprovalCategoryQueryDTO dto = mapper.findById(id);
		if(dto == null) {
			throw new BusinessException(ResponseCode.NOT_FOUND);
		}
		return dto;
	}

	@Override
	public List<ApprovalCategoryQueryDTO> findByCategoryId(Integer categoryId) {
		List<ApprovalCategoryQueryDTO> dtoList = mapper.findByCategoryId(categoryId);
		return dtoList;
	}

	@Override
	public List<ApprovalCategoryQueryDTO> searchByName(String name) {
		List<ApprovalCategoryQueryDTO> dtoList = mapper.searchByName(name);
		return dtoList;
	}
}
