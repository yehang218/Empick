package com.piveguyz.empickbackend.employment.introduce.query.service;

import com.piveguyz.empickbackend.employment.introduce.query.dto.IntroduceTemplateItemResponseQueryDTO;

import java.util.List;

public interface IntroduceTemplateItemResponseQueryService {
    List<IntroduceTemplateItemResponseQueryDTO> findAllIntroduceTemplateItemResponse();
}
