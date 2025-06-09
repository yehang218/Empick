package com.piveguyz.empickbackend.employment.introduce.query.mapper;

import com.piveguyz.empickbackend.employment.introduce.query.dto.IntroduceStandardItemQueryDTO;
import com.piveguyz.empickbackend.employment.introduce.query.dto.IntroduceStandardQueryDTO;

import java.util.List;

public interface IntroduceStandardMapper {
    List<IntroduceStandardQueryDTO> findAllIntroduceStandard();

    List<IntroduceStandardItemQueryDTO> findAllIntroduceStandardItem();
}
