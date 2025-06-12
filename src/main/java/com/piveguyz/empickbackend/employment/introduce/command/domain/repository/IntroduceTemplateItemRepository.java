package com.piveguyz.empickbackend.employment.introduce.command.domain.repository;

import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceTemplateItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntroduceTemplateItemRepository extends JpaRepository<IntroduceTemplateItemEntity, Integer> {

    boolean existsByTitle(String title);
}
