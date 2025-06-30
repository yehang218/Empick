package com.piveguyz.empickbackend.employment.introduce.command.domain.repository;

import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceStandardCommandDTO;
import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceStandardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntroduceStandardRepository extends JpaRepository<IntroduceStandardEntity, Integer> {
    boolean existsByContent(String content);
}
