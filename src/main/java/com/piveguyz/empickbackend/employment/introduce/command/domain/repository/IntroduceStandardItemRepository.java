package com.piveguyz.empickbackend.employment.introduce.command.domain.repository;

import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceStandardItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntroduceStandardItemRepository extends JpaRepository<IntroduceStandardItemEntity, Integer> {


    boolean existsByContent(String content);
}
