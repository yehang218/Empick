package com.piveguyz.empickbackend.orgstructure.department.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.department.command.domain.aggregate.DeptChangeHistoryEntity;
import com.piveguyz.empickbackend.orgstructure.department.command.domain.repository.DeptChangeHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeptChangeHistoryCommandServiceImpl implements DeptChangeHistoryCommandService {

    private final DeptChangeHistoryRepository deptChangeHistoryRepository;

    @Override
    public DeptChangeHistoryEntity createDeptChangeHistory(
            Integer memberId,
            String deptName,
            String positionName,
            String jobName,
            String rankName,
            LocalDateTime workStartAt) {
        DeptChangeHistoryEntity history = new DeptChangeHistoryEntity(
                memberId,
                deptName,
                positionName,
                jobName,
                rankName,
                workStartAt,
                null
        );
        return deptChangeHistoryRepository.save(history);
    }

    @Override
    public void endDeptChangeHistory(Integer historyId, LocalDateTime workEndAt) {
        DeptChangeHistoryEntity history = deptChangeHistoryRepository.findById(historyId).orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        history.endWork(workEndAt);
    }
}
