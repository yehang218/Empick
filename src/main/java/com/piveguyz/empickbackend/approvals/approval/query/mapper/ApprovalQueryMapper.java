package com.piveguyz.empickbackend.approvals.approval.query.mapper;

import com.piveguyz.empickbackend.approvals.approval.query.dto.ApprovalQueryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApprovalQueryMapper {

    List<ApprovalQueryDTO> findAll();

    ApprovalQueryDTO findById(Integer id);

    List<ApprovalQueryDTO> findByCategoryId(Integer categoryId);

    List<ApprovalQueryDTO> findByWriterId(Integer writerId);
}
