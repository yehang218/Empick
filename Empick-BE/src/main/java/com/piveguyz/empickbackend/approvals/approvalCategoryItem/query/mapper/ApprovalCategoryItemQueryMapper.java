package com.piveguyz.empickbackend.approvals.approvalCategoryItem.query.mapper;

import com.piveguyz.empickbackend.approvals.approvalCategoryItem.query.dto.ApprovalCategoryItemQueryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApprovalCategoryItemQueryMapper {

    List<ApprovalCategoryItemQueryDTO> findAll();

    ApprovalCategoryItemQueryDTO findById(Integer id);

    List<ApprovalCategoryItemQueryDTO> findByCategoryId(Integer categoryId);
}
