package com.piveguyz.empickbackend.rank.command.domain.repository;

import com.piveguyz.empickbackend.rank.command.domain.aggregate.RankEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankRepository extends JpaRepository<RankEntity, Integer> {
}
