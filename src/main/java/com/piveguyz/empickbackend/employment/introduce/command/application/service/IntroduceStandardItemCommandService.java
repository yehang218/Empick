package com.piveguyz.empickbackend.employment.introduce.command.application.service;

import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceStandardItemCommandDTO;
import jakarta.validation.Valid;

public interface IntroduceStandardItemCommandService {
    IntroduceStandardItemCommandDTO create(@Valid IntroduceStandardItemCommandDTO dto);

    int delete(int id);

    IntroduceStandardItemCommandDTO update(int id, @Valid IntroduceStandardItemCommandDTO dto);
}
