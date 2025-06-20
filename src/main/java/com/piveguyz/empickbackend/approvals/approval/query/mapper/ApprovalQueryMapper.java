package com.piveguyz.empickbackend.approvals.approval.query.mapper;

import com.piveguyz.empickbackend.approvals.approval.query.dto.ApprovalLineDetailDTO;
import com.piveguyz.empickbackend.approvals.approval.query.dto.ApprovalLineQueryDTO;
import com.piveguyz.empickbackend.approvals.approval.query.dto.ApprovalQueryDTO;
import com.piveguyz.empickbackend.approvals.approval.query.dto.ApprovalReceivedDetailQueryDTO;
import com.piveguyz.empickbackend.approvals.approval.query.dto.ApprovalReceivedQueryDTO;
import com.piveguyz.empickbackend.approvals.approval.query.dto.ApprovalRequestedListQueryDTO;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApprovalQueryMapper {

    List<ApprovalQueryDTO> findAll();

    ApprovalQueryDTO findById(Integer id);

    List<ApprovalQueryDTO> findByCategoryId(Integer categoryId);

    List<ApprovalQueryDTO> findByWriterId(Integer writerId);

    List<ApprovalReceivedQueryDTO> findReceivedApprovals(Integer memberId);

    List<ApprovalLineQueryDTO> selectApprovalLinePreview(Integer categoryId, Integer writerId);

    List<ApprovalRequestedListQueryDTO> findRequestedApprovals(Integer memberId);

    ApprovalReceivedDetailQueryDTO findApprovalBasicDetail(Integer approvalId);
    List<ApprovalLineDetailDTO> findApproverDetails(Integer approvalId);
    boolean isMyTurn(Integer approvalId, Integer memberId);
}
