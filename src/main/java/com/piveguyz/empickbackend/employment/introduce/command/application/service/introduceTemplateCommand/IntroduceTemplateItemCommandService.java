package com.piveguyz.empickbackend.employment.introduce.command.application.service.introduceTemplateCommand;

import com.piveguyz.empickbackend.employment.introduce.command.application.dto.introduceTemplate.IntroduceTemplateItemCommandDTO;

public interface IntroduceTemplateItemCommandService {
    IntroduceTemplateItemCommandDTO create(IntroduceTemplateItemCommandDTO dto);

    int delete(int id);
}
