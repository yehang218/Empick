package com.piveguyz.empickbackend.employment.introduce.command.domain.repository;

import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceTemplateItemResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntroduceTemplateItemResponseCommandRepository extends JpaRepository<IntroduceTemplateItemResponseEntity, Integer> {
    IntroduceTemplateItemResponseEntity save(IntroduceTemplateItemResponseEntity entity);
}
