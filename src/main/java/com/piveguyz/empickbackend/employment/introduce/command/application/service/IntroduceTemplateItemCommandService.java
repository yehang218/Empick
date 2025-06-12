package com.piveguyz.empickbackend.employment.introduce.command.application.service;

import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceTemplateItemCommandDTO;

public interface IntroduceTemplateItemCommandService {
    IntroduceTemplateItemCommandDTO create(IntroduceTemplateItemCommandDTO dto);

    int delete(int id);
}
