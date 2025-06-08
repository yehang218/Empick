package com.piveguyz.empickbackend.position.command.domain.repository;

import com.piveguyz.empickbackend.position.command.domain.aggregate.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<PositionEntity, Integer> {
}
