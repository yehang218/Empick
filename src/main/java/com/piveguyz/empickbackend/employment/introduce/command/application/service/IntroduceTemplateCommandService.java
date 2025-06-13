package com.piveguyz.empickbackend.employment.introduce.command.application.service;


public interface IntroduceTemplateCommandService {
import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceTemplateCommandDTO;
import jakarta.validation.Valid;

public interface IntroduceTemplateCommandService {
    IntroduceTemplateCommandDTO create(@Valid IntroduceTemplateCommandDTO dto);

    int delete(int id);
}
