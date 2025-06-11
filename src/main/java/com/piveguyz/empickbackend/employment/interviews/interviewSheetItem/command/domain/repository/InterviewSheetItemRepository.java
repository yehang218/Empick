package com.piveguyz.empickbackend.employment.interviews.interviewSheetItem.command.domain.repository;

import com.piveguyz.empickbackend.employment.interviews.interviewSheetItem.command.domain.aggregate.InterviewSheetItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewSheetItemRepository extends JpaRepository<InterviewSheetItemEntity, Integer> {
    boolean existsBySheetIdAndCriteriaId(Integer sheetId, Integer criteriaId);
}
