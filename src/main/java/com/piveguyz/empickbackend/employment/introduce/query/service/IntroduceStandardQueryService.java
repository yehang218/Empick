package com.piveguyz.empickbackend.employment.introduce.query.service;

import com.piveguyz.empickbackend.employment.introduce.query.dto.IntroduceStandardItemQueryDTO;
import com.piveguyz.empickbackend.employment.introduce.query.dto.IntroduceStandardQueryDTO;

import java.util.List;

public interface IntroduceStandardQueryService {
    List<IntroduceStandardQueryDTO> findAllIntroduceStandard();

    List<IntroduceStandardItemQueryDTO> findAllIntroduceStandardItem();

    IntroduceStandardQueryDTO getIntroduceStandardById(int id);
}
