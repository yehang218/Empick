package com.piveguyz.empickbackend.employment.introduce.command.domain.repository;

import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceRatingResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntroduceRatingResultRepository extends JpaRepository<IntroduceRatingResultEntity, Integer> {

}
