package com.piveguyz.empickbackend.approvals.approvalContent.query.mapper;

import com.piveguyz.empickbackend.approvals.approvalContent.query.dto.ApprovalContentQueryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApprovalContentQueryMapper {

    List<ApprovalContentQueryDTO> findAll();

    ApprovalContentQueryDTO findById(Integer id);

    List<ApprovalContentQueryDTO> findByApprovalId(Integer approvalId);
}
