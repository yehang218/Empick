package com.piveguyz.empickbackend.employment.introduce.command.application.service;

import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceTemplateItemResponseCommandDTO;
import jakarta.validation.Valid;

public interface IntroduceTemplateItemResponseCommandService {
    IntroduceTemplateItemResponseCommandDTO create(@Valid IntroduceTemplateItemResponseCommandDTO dto);
}
