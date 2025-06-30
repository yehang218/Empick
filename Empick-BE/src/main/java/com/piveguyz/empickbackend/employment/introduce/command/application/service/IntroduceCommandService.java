package com.piveguyz.empickbackend.employment.introduce.command.application.service;

import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceCommandDTO;
import jakarta.validation.Valid;

public interface IntroduceCommandService {
    IntroduceCommandDTO create(@Valid IntroduceCommandDTO dto);
}
