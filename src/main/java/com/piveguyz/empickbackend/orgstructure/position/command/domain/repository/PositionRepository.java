package com.piveguyz.empickbackend.orgstructure.position.command.domain.repository;

import com.piveguyz.empickbackend.orgstructure.position.command.domain.aggregate.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<PositionEntity, Integer> {
}
