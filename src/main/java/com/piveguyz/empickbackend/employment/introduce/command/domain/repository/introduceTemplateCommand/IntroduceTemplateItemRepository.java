package com.piveguyz.empickbackend.employment.introduce.command.domain.repository.introduceTemplateCommand;

import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.introduceTemplateCommand.IntroduceTemplateItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntroduceTemplateItemRepository extends JpaRepository<IntroduceTemplateItemEntity, Integer> {

    boolean existsByTitle(String title);
}
