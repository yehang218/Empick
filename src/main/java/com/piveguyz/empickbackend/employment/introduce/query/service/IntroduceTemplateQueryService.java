package com.piveguyz.empickbackend.employment.introduce.query.service;

import com.piveguyz.empickbackend.employment.introduce.query.dto.IntroduceTemplateQueryDTO;

import java.util.List;

public interface IntroduceTemplateQueryService {
    List<IntroduceTemplateQueryDTO> findAllIntroduceTemplate();

    List<IntroduceTemplateQueryDTO> findAllIntroduceTemplateItem();

    IntroduceTemplateQueryDTO getIntroduceTemplateById(int id);
}
