package com.piveguyz.empickbackend.employment.introduce.command.application.service;

import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceStandardCommandDTO;

public interface IntroduceStandardCommandService {
    IntroduceStandardCommandDTO create(IntroduceStandardCommandDTO dto);
    int delete(int id);
}
