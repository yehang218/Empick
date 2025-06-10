package com.piveguyz.empickbackend.orgstructure.department.command.application.service;

import com.piveguyz.empickbackend.orgstructure.department.command.domain.aggregate.DeptChangeHistoryEntity;

import java.time.LocalDateTime;

public interface DeptChangeHistoryCommandService {
    DeptChangeHistoryEntity createDeptChangeHistory(Integer memberId,
                                                    String deptName,
                                                    String positionName,
                                                    String jobName,
                                                    String rankName,
                                                    LocalDateTime workStartAt);

    void endDeptChangeHistory(Integer historyId, LocalDateTime workEndAt);
}
